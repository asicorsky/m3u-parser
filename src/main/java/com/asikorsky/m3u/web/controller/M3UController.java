package com.asikorsky.m3u.web.controller;

import com.asikorsky.m3u.service.m3u.M3UService;
import com.asikorsky.m3u.web.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(Navigation.M3U)
public class M3UController {

	private final M3UService m3UService;

	@Autowired
	public M3UController(M3UService m3UService) {

		this.m3UService = m3UService;
	}

	@PostMapping(Navigation.UPLOAD)
	public ResponseEntity<?> upload(@RequestParam(Navigation.FILE) MultipartFile file) throws IOException {

		m3UService.parseAndSave(file.getInputStream(), file.getOriginalFilename());
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = Navigation.DOWNLOAD_FOR_PLAYLIST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> download(@PathVariable(Navigation.PLAYLIST_ID) long playlistId) throws IOException {

		File file = m3UService.createM3U8(playlistId);
		return ResponseEntity.ok(new FileSystemResource(file));
	}
}
