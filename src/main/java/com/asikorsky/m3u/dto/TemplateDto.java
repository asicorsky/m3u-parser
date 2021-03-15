package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateDto {

	private final Long id;
	private final String name;

	@JsonCreator
	public TemplateDto(@JsonProperty("id") Long id, @JsonProperty("name") String name) {

		this.id = id;
		this.name = name;
	}

	public Long getId() {

		return id;
	}

	public String getName() {

		return name;
	}
}
