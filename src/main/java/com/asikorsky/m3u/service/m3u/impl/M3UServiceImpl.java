package com.asikorsky.m3u.service.m3u.impl;

import com.asikorsky.m3u.domain.entity.CategoryEntity;
import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.repository.CategoryRepository;
import com.asikorsky.m3u.domain.repository.ChannelRepository;
import com.asikorsky.m3u.domain.repository.PlaylistRepository;
import com.asikorsky.m3u.dto.ChannelStatus;
import com.asikorsky.m3u.service.m3u.M3UService;
import com.asikorsky.m3u.service.mapper.EntityMapper;
import com.asikorsky.m3u.service.transactional.TransactionalInvoker;
import com.asikorsky.m3u.utils.M3UUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class M3UServiceImpl implements M3UService {

	private static final String M3_STARTING_TAG = "#EXTM3U";
	private static final String GROUP_STARTING_TAG = "#EXTGRP:";
	private static final String CHANNEL_STARTING_TAG = "#EXTINF:";
	private static final String HISTORY_TAG_STARTING = "tvg-rec=\"";
	private static final String M3U8_EXT = "m3u8";

	private final TransactionalInvoker transactionalInvoker;
	private final PlaylistRepository playlistRepository;
	private final CategoryRepository categoryRepository;
	private final ChannelRepository channelRepository;

	@Autowired
	public M3UServiceImpl(TransactionalInvoker transactionalInvoker, PlaylistRepository playlistRepository, CategoryRepository categoryRepository,
			ChannelRepository channelRepository) {

		this.transactionalInvoker = transactionalInvoker;
		this.playlistRepository = playlistRepository;
		this.categoryRepository = categoryRepository;
		this.channelRepository = channelRepository;
	}

	@Override
	public void parseAndSave(InputStream stream, String name) {

		transactionalInvoker.executeInNewTransaction(() -> playlistRepository.deleteByName(name));

		PlaylistEntity playlist = playlistRepository.save(EntityMapper.toPlaylistEntity(name));
		var rawChannels = parse(stream);
		rawChannels.forEach(rawChannel -> {
			CategoryEntity category = categoryRepository.findByPlaylistAndName(playlist, rawChannel.groupName)
					.orElseGet(() -> categoryRepository.save(EntityMapper.toCategoryEntity(playlist, rawChannel.groupName)));
			var channels = category.getChannels();
			boolean nonMatched = channels.stream().noneMatch(channel -> channel.getName().equals(rawChannel.channelName));
			if (nonMatched) {
				ChannelEntity channel = mergeChannel(rawChannel);
				channels.add(channel);
				categoryRepository.save(category);
			}
		});
	}

	@Override
	public File createM3U8(long playlistId) {

		try {
			PlaylistEntity entity = playlistRepository.findById(playlistId).orElseThrow();
			File file = Files.createTempFile(entity.getName() + "-", M3U8_EXT).toFile();
			FileWriter fw = new FileWriter(file);
			PrintWriter writer = new PrintWriter(fw);
			try (fw; writer) {
				writer.println(M3_STARTING_TAG);
				var categories = entity.getCategories();
				categories.forEach(category -> {
					String categoryName = category.getName();
					var channels = category.getChannels();
					channels.forEach(channel -> {
						String info = wrapInfo(channel.getName(), channel.getHistory());
						String groupName = wrapCategory(categoryName);
						writer.println(info);
						writer.println(groupName);
						writer.println(channel.getUrl());
					});
					writer.flush();
				});
			}
			return file;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<RawChannel> parse(InputStream stream) {

		var rawChannels = new ArrayList<RawChannel>();
		try (InputStreamReader isr = new InputStreamReader(stream); BufferedReader reader = new BufferedReader(isr)) {
			String line;
			RawChannel lastChannel = null;
			while (Objects.nonNull(line = reader.readLine())) {
				if (M3_STARTING_TAG.equals(line)) {
					continue;
				}
				if (line.startsWith(GROUP_STARTING_TAG)) {
					Objects.requireNonNull(lastChannel).groupName = line.substring(GROUP_STARTING_TAG.length());

				} else if (M3UUtils.isUri(line)) {
					Objects.requireNonNull(lastChannel).url = line;

				} else if (line.startsWith(CHANNEL_STARTING_TAG)) {
					if (Objects.nonNull(lastChannel)) {
						rawChannels.add(lastChannel);
					}
					lastChannel = new RawChannel();
					String channelInfo = line.substring(CHANNEL_STARTING_TAG.length());
					String[] infoArray = channelInfo.split(",");
					String channelName = infoArray[1];
					if (channelInfo.contains(HISTORY_TAG_STARTING)) {
						String historyTag = infoArray[0].split(" ")[1];
						lastChannel.history = Integer.parseInt(historyTag.substring(HISTORY_TAG_STARTING.length(), historyTag.length() - 1));
					} else {
						lastChannel.history = 0;
					}
					lastChannel.channelName = channelName;
				}
			}
			rawChannels.add(lastChannel);
			return rawChannels;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String wrapInfo(String name, int history) {

		return CHANNEL_STARTING_TAG + "0 tvg-rec=\"" + history + "\"," + name;
	}

	private static String wrapCategory(String category) {

		return GROUP_STARTING_TAG + category;
	}

	private ChannelEntity mergeChannel(RawChannel rawChannel) {

		var optional = channelRepository.findByName(rawChannel.channelName);
		ChannelEntity channel = optional.map(entity -> {
			entity.setHistory(rawChannel.history);
			entity.setUrl(rawChannel.url);
			return entity;
		}).orElseGet(() -> EntityMapper.toChannelEntity(rawChannel.channelName, rawChannel.history, rawChannel.url, ChannelStatus.NOT_VALIDATED.text(), null));

		return channelRepository.save(channel);
	}

	private static class RawChannel {

		private String channelName;
		private String groupName;
		private String url;
		private int history;

		@Override
		public String toString() {

			return "RawChannel{" + "channelName='" + channelName + '\'' + ", groupName='" + groupName + '\'' + ", url='" + url + '\'' + ", history=" + history + '}';
		}
	}
}
