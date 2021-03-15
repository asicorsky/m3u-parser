package com.asikorsky.m3u.service.playlist.impl;

import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.repository.PlaylistRepository;
import com.asikorsky.m3u.dto.ChannelDto;
import com.asikorsky.m3u.dto.ChannelFilter;
import com.asikorsky.m3u.dto.ChannelStatus;
import com.asikorsky.m3u.dto.PlaylistDto;
import com.asikorsky.m3u.dto.builder.ChannelFilterBuilder;
import com.asikorsky.m3u.service.category.CategoryService;
import com.asikorsky.m3u.service.channel.ChannelService;
import com.asikorsky.m3u.service.mapper.DtoMapper;
import com.asikorsky.m3u.service.playlist.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final ChannelService channelService;
	private final CategoryService categoryService;

	@Autowired
	public PlaylistServiceImpl(PlaylistRepository playlistRepository, ChannelService channelService, CategoryService categoryService) {

		this.playlistRepository = playlistRepository;
		this.channelService = channelService;
		this.categoryService = categoryService;
	}

	@Override
	public List<PlaylistDto> getPlaylists() {

		return playlistRepository.findAll().stream().map(DtoMapper::toPlaylistDto).collect(Collectors.toList());
	}

	@Override
	public void remove(long id) {

		playlistRepository.deleteById(id);
	}

	@Override
	public void detachChannel(long playlistId, long channelId) {

		PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();
		var categories = playlist.getCategories();
		categories.forEach(category -> categoryService.detachChannel(category.getId(), channelId));
	}

	@Override
	public List<ChannelDto> validate(long id, boolean download) {

		ChannelFilter filter = ChannelFilterBuilder.newBuilder().playlistIds(Collections.singletonList(id)).build();
		var channels = channelService.validate(filter, download);
		return channels.stream().filter(channel -> ChannelStatus.EMPTY_DATA.equals(channel.getStatus())).collect(Collectors.toList());
	}
}
