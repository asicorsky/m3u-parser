package com.asikorsky.m3u.service.channel.impl;

import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.expressions.ChannelExpression;
import com.asikorsky.m3u.domain.repository.ChannelRepository;
import com.asikorsky.m3u.dto.ChannelDto;
import com.asikorsky.m3u.dto.ChannelFilter;
import com.asikorsky.m3u.dto.ChannelStatus;
import com.asikorsky.m3u.service.channel.ChannelService;
import com.asikorsky.m3u.service.mapper.DtoMapper;
import com.asikorsky.m3u.utils.ConcurrentUtils;
import com.asikorsky.m3u.utils.M3UUtils;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
public class ChannelServiceImpl implements ChannelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelServiceImpl.class);
	private static final int DATA_SIZE = 1024 * 1024 * 2;

	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final String TS_EXT = "ts";
	private static final List<String> SUPPORTED_PROTOCOLS = List.of(HTTP, HTTPS);

	private final ChannelRepository channelRepository;

	@Autowired
	public ChannelServiceImpl(ChannelRepository channelRepository) {

		this.channelRepository = channelRepository;
	}

	@Override
	public List<ChannelDto> getChannels(ChannelFilter filter) {

		ChannelExpression expression = new ChannelExpression(filter);
		Predicate predicate = expression.predicate();
		var orders = expression.orders();
		var entities = channelRepository.findAll(predicate, orders);
		return StreamSupport.stream(entities.spliterator(), false).map(DtoMapper::toChannelDto).collect(Collectors.toList());
	}

	@Override
	public List<ChannelDto> validate(ChannelFilter filter, boolean download) {

		ChannelExpression expression = new ChannelExpression(filter);
		var entities = channelRepository.findAll(expression.predicate());
		var futures = StreamSupport.stream(entities.spliterator(), false).map(entity -> CompletableFuture.supplyAsync(() -> {
			ChannelStatus status = status(entity.getUrl());
			entity.setStatus(status.text());
			channelRepository.save(entity);
			return DtoMapper.toChannelDto(entity);
		})).collect(Collectors.toList());
		var channels = ConcurrentUtils.awaitAndMap(futures);
		if (download) {
			//TODO: calculate average
			download(entities);
		}
		return channels;
	}

	private ChannelStatus status(String uri) {

		if (SUPPORTED_PROTOCOLS.stream().anyMatch(uri::startsWith)) {
			try {
				URL url = new URL(uri);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				int responseCode = connection.getResponseCode();
				if (responseCode != 200) {
					return ChannelStatus.EMPTY_DATA;
				}
				if (uri.endsWith(TS_EXT)) {
					return ChannelStatus.VALIDATED;
				}
				try (InputStream is = connection.getInputStream(); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
					String line;
					while (Objects.nonNull(line = br.readLine())) {
						if (M3UUtils.isUri(line)) {
							return status(line);
						}
					}
					return ChannelStatus.EMPTY_DATA;
				}
			} catch (IOException e) {
				LOGGER.error("Exceptionally", e);
				throw new RuntimeException(e);
			}
		} else {
			return ChannelStatus.VALIDATION_NOT_SUPPORTED;
		}
	}

	private Map<Integer, List<Pair<ChannelEntity, BigDecimal>>> download(Iterable<ChannelEntity> entities) {

		var summary = new HashMap<Integer, List<Pair<ChannelEntity, BigDecimal>>>();
		IntStream.range(0, 3).forEach(attempt -> {
			LOGGER.info("Attempt {} started", attempt);
			var pairs = new ArrayList<Pair<ChannelEntity, BigDecimal>>();
			summary.put(attempt, pairs);
			entities.forEach(channel -> {
				try {
					if (ChannelStatus.VALIDATED.text().equals(channel.getStatus())) {
						String uri = channel.getUrl();
						URL url = new URL(uri);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						try (InputStream is = connection.getInputStream(); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
							String line;
							while (Objects.nonNull(line = br.readLine())) {
								if (M3UUtils.isUri(line)) {
									BigDecimal coefficient = downloadPart(line);
									pairs.add(Pair.of(channel, coefficient));
									break;
								}
							}
						}
					}
				} catch (IOException e) {
					LOGGER.error("Exceptionally", e);
				}
			});

			LOGGER.info("Attempt {} finished", attempt);
		});
		return summary;
	}

	private BigDecimal downloadPart(String videoUrl) {

		try {
			long begin = System.currentTimeMillis();
			URL url = new URL(videoUrl);
			try (InputStream is = url.openStream(); BufferedInputStream bis = new BufferedInputStream(is)) {
				if ((bis.read(new byte[DATA_SIZE], 0, DATA_SIZE)) != -1) {
					long end = System.currentTimeMillis();
					long downloadTime = end - begin;
					return new BigDecimal(DATA_SIZE / (float) downloadTime);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Cannot download the file", e);
		}
		return new BigDecimal(-1);
	}
}
