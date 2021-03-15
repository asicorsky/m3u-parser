package com.asikorsky.m3u.service.template.impl;

import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.entity.TemplateEntity;
import com.asikorsky.m3u.domain.repository.ChannelRepository;
import com.asikorsky.m3u.domain.repository.PlaylistRepository;
import com.asikorsky.m3u.domain.repository.TemplateRepository;
import com.asikorsky.m3u.dto.TemplateComparisonDto;
import com.asikorsky.m3u.dto.TemplateDto;
import com.asikorsky.m3u.service.mapper.DtoMapper;
import com.asikorsky.m3u.service.mapper.EntityMapper;
import com.asikorsky.m3u.service.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {

	private final PlaylistRepository playlistRepository;
	private final TemplateRepository templateRepository;
	private final ChannelRepository channelRepository;

	@Autowired
	public TemplateServiceImpl(PlaylistRepository playlistRepository, TemplateRepository templateRepository, ChannelRepository channelRepository) {

		this.playlistRepository = playlistRepository;
		this.templateRepository = templateRepository;
		this.channelRepository = channelRepository;
	}

	@Override
	public void createFromPlaylist(long playlistId, TemplateDto template) {

		PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();

		var names = new HashSet<String>();
		var channels = playlist.getCategories().stream().flatMap(categories -> categories.getChannels().stream()).filter(channel -> names.add(channel.getName()))
				.collect(Collectors.toList());
		TemplateEntity entity = EntityMapper.toTemplateEntity(template.getName());
		entity.setChannels(channels);
		templateRepository.save(entity);
	}

	@Override
	public List<TemplateDto> getTemplates() {

		var entities = templateRepository.findAll();
		return entities.stream().map(DtoMapper::toTemplateDto).collect(Collectors.toList());
	}

	@Override
	public TemplateComparisonDto checkChannels(long playlistId, long templateId) {

		var templateChannelEntities = channelRepository.getForTemplate(templateId);
		var templateMap = new HashMap<String, ChannelEntity>();
		templateChannelEntities.forEach(entity -> templateMap.put(entity.getName(), entity));

		var channelMap = new HashMap<String, ChannelEntity>();
		var channelEntities = channelRepository.getForPlaylist(playlistId);
		channelEntities.forEach(entity -> channelMap.put(entity.getName(), entity));

		var missingOnPlaylistNames = new TreeSet<>(templateMap.keySet());
		missingOnPlaylistNames.removeAll(channelMap.keySet());

		var missionOnTemplateNames = new TreeSet<>(channelMap.keySet());
		missionOnTemplateNames.removeAll(templateMap.keySet());

		var missingOnPlaylist = missingOnPlaylistNames.stream().map(templateMap::get).map(DtoMapper::toChannelDto).collect(Collectors.toList());
		var missingOnTemplate = missionOnTemplateNames.stream().map(channelMap::get).map(DtoMapper::toChannelDto).collect(Collectors.toList());
		return new TemplateComparisonDto(missingOnTemplate, missingOnPlaylist);
	}

	@Override
	public void attachChannel(long templateId, long channelId) {

		TemplateEntity template = templateRepository.findById(templateId).orElseThrow();
		var channels = template.getChannels();
		boolean nonMatched = channels.stream().noneMatch(channel -> channel.getId().equals(channelId));
		if (nonMatched) {
			ChannelEntity channel = channelRepository.findById(channelId).orElseThrow();
			channels.add(channel);
			templateRepository.save(template);
		}
	}

	@Override
	public void detachChannel(long templateId, long channelId) {

		TemplateEntity template = templateRepository.findById(templateId).orElseThrow();
		var channels = template.getChannels();
		channels.removeIf(channel -> channel.getId().equals(channelId));
		templateRepository.save(template);
	}
}
