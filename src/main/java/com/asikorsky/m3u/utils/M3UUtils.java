package com.asikorsky.m3u.utils;

import java.util.List;

public interface M3UUtils {

	List<String> PROTOCOLS = List.of("http", "https", "udp", "rtmp", "rtp", "rtsp", "mms", "acestream");

	static boolean isUri(String line) {

		return PROTOCOLS.stream().anyMatch(line::startsWith);
	}
}
