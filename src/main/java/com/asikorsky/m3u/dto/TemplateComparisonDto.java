package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TemplateComparisonDto {

	private final List<ChannelDto> missingOnTemplate;
	private final List<ChannelDto> missingOnPlaylist;

	@JsonCreator
	public TemplateComparisonDto(@JsonProperty("missingOnTemplate") List<ChannelDto> missingOnTemplate, @JsonProperty("missingOnPlaylist") List<ChannelDto> missingOnPlaylist) {

		this.missingOnTemplate = missingOnTemplate;
		this.missingOnPlaylist = missingOnPlaylist;
	}

	public List<ChannelDto> getMissingOnTemplate() {

		return missingOnTemplate;
	}

	public List<ChannelDto> getMissingOnPlaylist() {

		return missingOnPlaylist;
	}
}
