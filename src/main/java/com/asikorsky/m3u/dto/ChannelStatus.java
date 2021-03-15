package com.asikorsky.m3u.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ChannelStatus {

	NOT_VALIDATED("not_validated"),
	VALIDATED("validated"),
	VALIDATION_NOT_SUPPORTED("validation_not_supported"),
	EMPTY_DATA("empty_data");

	private final String text;

	ChannelStatus(String text) {

		this.text = text;
	}

	@JsonValue
	public String text() {

		return text;
	}

	public static ChannelStatus byText(String text) {

		return Arrays.stream(values()).filter(value -> value.text.equals(text)).findAny().orElseThrow();
	}
}
