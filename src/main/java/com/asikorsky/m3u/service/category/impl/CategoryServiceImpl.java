package com.asikorsky.m3u.service.category.impl;

import com.asikorsky.m3u.domain.entity.CategoryEntity;
import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.repository.CategoryRepository;
import com.asikorsky.m3u.domain.repository.ChannelRepository;
import com.asikorsky.m3u.domain.repository.PlaylistRepository;
import com.asikorsky.m3u.dto.CategoryDto;
import com.asikorsky.m3u.service.category.CategoryService;
import com.asikorsky.m3u.service.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final PlaylistRepository playlistRepository;
	private final ChannelRepository channelRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, PlaylistRepository playlistRepository, ChannelRepository channelRepository) {

		this.categoryRepository = categoryRepository;
		this.playlistRepository = playlistRepository;
		this.channelRepository = channelRepository;
	}

	@Override
	public List<CategoryDto> getForPlaylist(long playlistId) {

		PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();
		var entities = categoryRepository.findByPlaylist(playlist);
		return entities.stream().map(DtoMapper::toCategoryDto).collect(Collectors.toList());
	}

	@Override
	public void detachChannel(long categoryId, long channelId) {

		CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow();
		var channels = category.getChannels();
		channels.removeIf(channel -> channel.getId().equals(channelId));
		categoryRepository.save(category);
	}

	@Override
	public void attachChannel(long categoryId, long channelId) {

		CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow();
		var channels = category.getChannels();
		boolean nonMatched = channels.stream().noneMatch(channel -> channel.getId().equals(channelId));
		if (nonMatched) {
			ChannelEntity channel = channelRepository.findById(channelId).orElseThrow();
			channels.add(channel);
			categoryRepository.save(category);
		}
	}
}
