package com.asikorsky.m3u.web.controller;

import com.asikorsky.m3u.dto.ChannelFilter;
import com.asikorsky.m3u.service.channel.ChannelService;
import com.asikorsky.m3u.web.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Navigation.CHANNEL)
public class ChannelController {

	private final ChannelService channelService;

	@Autowired
	public ChannelController(ChannelService channelService) {

		this.channelService = channelService;
	}

	@PostMapping(Navigation.FILTER)
	public ResponseEntity<?> load(@RequestBody ChannelFilter filter) {

		var channels = channelService.getChannels(filter);
		return ResponseEntity.ok(channels);
	}
}
