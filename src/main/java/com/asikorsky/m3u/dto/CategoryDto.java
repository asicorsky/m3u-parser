package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDto {

	private final Long id;
	private final String name;
	private final PlaylistDto playlist;

	@JsonCreator
	public CategoryDto(@JsonProperty("id") Long id, @JsonProperty("name") String name, @JsonProperty("playlist") PlaylistDto playlist) {

		this.id = id;
		this.name = name;
		this.playlist = playlist;
	}

	public Long getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public PlaylistDto getPlaylist() {

		return playlist;
	}
}
