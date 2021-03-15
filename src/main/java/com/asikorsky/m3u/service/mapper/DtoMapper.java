package com.asikorsky.m3u.service.mapper;

import com.asikorsky.m3u.domain.entity.CategoryEntity;
import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.entity.TemplateEntity;
import com.asikorsky.m3u.dto.CategoryDto;
import com.asikorsky.m3u.dto.ChannelDto;
import com.asikorsky.m3u.dto.ChannelStatus;
import com.asikorsky.m3u.dto.PlaylistDto;
import com.asikorsky.m3u.dto.TemplateDto;

import java.util.Objects;

public interface DtoMapper {

	static PlaylistDto toPlaylistDto(PlaylistEntity entity) {

		if (Objects.isNull(entity)) {
			return null;
		}
		return new PlaylistDto(entity.getId(), entity.getName());
	}

	static CategoryDto toCategoryDto(CategoryEntity entity) {

		if (Objects.isNull(entity)) {
			return null;
		}

		PlaylistDto playlist = toPlaylistDto(entity.getPlaylist());
		return new CategoryDto(entity.getId(), entity.getName(), playlist);
	}

	static ChannelDto toChannelDto(ChannelEntity entity) {

		if (Objects.isNull(entity)) {
			return null;
		}
		return new ChannelDto(entity.getId(), entity.getName(), entity.getHistory(), entity.getUrl(), ChannelStatus.byText(entity.getStatus()));
	}

	static TemplateDto toTemplateDto(TemplateEntity entity) {

		if (Objects.isNull(entity)) {
			return null;
		}
		return new TemplateDto(entity.getId(), entity.getName());
	}
}
