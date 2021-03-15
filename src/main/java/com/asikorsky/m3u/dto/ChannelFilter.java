package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class ChannelFilter {

	private final String name;
	private final Collection<Long> categoryIds;
	private final Collection<Long> playlistIds;
	private final Collection<Long> templateIds;
	private final Collection<ChannelStatus> statuses;

	@JsonCreator
	public ChannelFilter(@JsonProperty("name") String name, @JsonProperty("categoryIds") Collection<Long> categoryIds, @JsonProperty("playlistIds") Collection<Long> playlistIds,
			@JsonProperty("templateIds") Collection<Long> templateIds, @JsonProperty("statuses") Collection<ChannelStatus> statuses) {

		this.name = name;
		this.categoryIds = categoryIds;
		this.playlistIds = playlistIds;
		this.templateIds = templateIds;
		this.statuses = statuses;
	}

	public String getName() {

		return name;
	}

	public Collection<Long> getPlaylistIds() {

		return playlistIds;
	}

	public Collection<Long> getCategoryIds() {

		return categoryIds;
	}

	public Collection<Long> getTemplateIds() {

		return templateIds;
	}

	public Collection<ChannelStatus> getStatuses() {

		return statuses;
	}
}
