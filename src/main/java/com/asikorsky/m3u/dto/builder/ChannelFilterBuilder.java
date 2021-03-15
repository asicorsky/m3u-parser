package com.asikorsky.m3u.dto.builder;

import com.asikorsky.m3u.dto.ChannelFilter;
import com.asikorsky.m3u.dto.ChannelStatus;

import java.util.Collection;

public final class ChannelFilterBuilder {

	private String name;
	private Collection<Long> categoryIds;
	private Collection<Long> playlistIds;
	private Collection<Long> templateIds;
	private Collection<ChannelStatus> statuses;

	public static ChannelFilterBuilder newBuilder() {

		return new ChannelFilterBuilder();
	}

	public static ChannelFilterBuilder newBuilder(ChannelFilter filter) {

		return new ChannelFilterBuilder(filter);
	}

	private ChannelFilterBuilder() {

	}

	private ChannelFilterBuilder(ChannelFilter filter) {

		this.name = filter.getName();
		this.categoryIds = filter.getCategoryIds();
		this.playlistIds = filter.getPlaylistIds();
		this.templateIds = filter.getTemplateIds();
		this.statuses = filter.getStatuses();
	}

	public ChannelFilterBuilder name(String name) {

		this.name = name;
		return this;
	}

	public ChannelFilterBuilder categoryIds(Collection<Long> categoryIds) {

		this.categoryIds = categoryIds;
		return this;
	}

	public ChannelFilterBuilder playlistIds(Collection<Long> playlistIds) {

		this.playlistIds = playlistIds;
		return this;
	}

	public ChannelFilterBuilder templateIds(Collection<Long> templateIds) {

		this.templateIds = templateIds;
		return this;
	}

	public ChannelFilterBuilder statuses(Collection<ChannelStatus> statuses) {

		this.statuses = statuses;
		return this;
	}

	public ChannelFilter build() {

		return new ChannelFilter(name, categoryIds, playlistIds, templateIds, statuses);
	}
}
