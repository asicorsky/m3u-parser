package com.asikorsky.m3u.service.mapper;

import com.asikorsky.m3u.domain.entity.CategoryEntity;
import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import com.asikorsky.m3u.domain.entity.TemplateEntity;

import java.math.BigDecimal;

public interface EntityMapper {

	static PlaylistEntity toPlaylistEntity(String name) {

		return new PlaylistEntity(name);
	}

	static CategoryEntity toCategoryEntity(PlaylistEntity playlist, String name) {

		return new CategoryEntity(playlist, name);
	}

	static ChannelEntity toChannelEntity(String name, int history, String url, String status, BigDecimal connectionCoefficient) {

		return new ChannelEntity(name, history, url, status, connectionCoefficient);
	}

	static TemplateEntity toTemplateEntity(String name) {

		return new TemplateEntity(name);
	}
}
