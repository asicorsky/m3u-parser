package com.asikorsky.m3u.service.m3u;

import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;

@Transactional
public interface M3UService {

	void parseAndSave(InputStream stream, String name);

	File createM3U8(long playlistId);
}
