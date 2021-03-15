package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelDto {

	private final Long id;
	private final String name;
	private final int history;
	private final String url;
	private final ChannelStatus status;

	@JsonCreator
	public ChannelDto(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("history") int history, @JsonProperty("url") String url,
			@JsonProperty("status") ChannelStatus status) {

		this.id = id;
		this.name = name;
		this.history = history;
		this.url = url;
		this.status = status;
	}

	public Long getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public int getHistory() {

		return history;
	}

	public String getUrl() {

		return url;
	}

	public ChannelStatus getStatus() {

		return status;
	}

}
