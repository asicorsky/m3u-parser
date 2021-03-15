package com.asikorsky.m3u.web.controller;

import com.asikorsky.m3u.service.playlist.PlaylistService;
import com.asikorsky.m3u.web.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Navigation.PLAYLIST)
public class PlaylistController {

	private final PlaylistService playlistService;

	@Autowired
	public PlaylistController(PlaylistService playlistService) {

		this.playlistService = playlistService;
	}

	@GetMapping(Navigation.ALL)
	public ResponseEntity<?> getPlaylists() {

		var playlists = playlistService.getPlaylists();
		return ResponseEntity.ok(playlists);
	}

	@DeleteMapping(Navigation.BY_ID)
	public ResponseEntity<?> removePlaylist(@PathVariable(Navigation.ID) long id) {

		playlistService.remove(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping(Navigation.VALIDATE_BY_ID)
	public ResponseEntity<?> validate(@PathVariable(Navigation.ID) long id, @PathVariable(Navigation.DOWNLOAD) boolean download) {

		var channels = playlistService.validate(id, download);
		return ResponseEntity.ok(channels);
	}

	@DeleteMapping(Navigation.DETACH_CHANNEL_FROM_PLAYLIST)
	public ResponseEntity<?> detachChannel(@PathVariable(Navigation.PLAYLIST_ID) long playlistId, @PathVariable(Navigation.CHANNEL_ID) long channelId) {

		playlistService.detachChannel(playlistId, channelId);
		return ResponseEntity.ok().build();
	}
}
