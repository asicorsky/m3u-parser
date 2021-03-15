package com.asikorsky.m3u.web.controller;

import com.asikorsky.m3u.dto.TemplateComparisonDto;
import com.asikorsky.m3u.dto.TemplateDto;
import com.asikorsky.m3u.service.template.TemplateService;
import com.asikorsky.m3u.web.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Navigation.TEMPLATE)
public class TemplateController {

	private final TemplateService templateService;

	@Autowired
	public TemplateController(TemplateService templateService) {

		this.templateService = templateService;
	}

	@PostMapping(Navigation.FROM_PLAYLIST)
	public ResponseEntity<?> createFromPlaylist(@PathVariable(Navigation.PLAYLIST_ID) long playlistId, @RequestBody TemplateDto template) {

		templateService.createFromPlaylist(playlistId, template);
		return ResponseEntity.ok(HttpEntity.EMPTY);
	}

	@GetMapping(Navigation.ALL)
	public ResponseEntity<?> getTemplates() {

		var templates = templateService.getTemplates();
		return ResponseEntity.ok(templates);
	}

	@GetMapping(Navigation.CHECK_PLAYLIST_CHANNELS_FOR_TEMPLATE)
	public ResponseEntity<?> checkChannels(@PathVariable(Navigation.PLAYLIST_ID) long playlistId, @PathVariable(Navigation.TEMPLATE_ID) long templateId) {

		TemplateComparisonDto comparison = templateService.checkChannels(playlistId, templateId);
		return ResponseEntity.ok(comparison);
	}

	@PostMapping(Navigation.ATTACH_CHANNEL_TO_TEMPLATE)
	public ResponseEntity<?> attachChannel(@PathVariable(Navigation.TEMPLATE_ID) long templateId, @PathVariable(Navigation.CHANNEL_ID) long channelId) {

		templateService.attachChannel(templateId, channelId);
		return ResponseEntity.ok(HttpEntity.EMPTY);
	}

	@DeleteMapping(Navigation.DETACH_CHANNEL_FROM_TEMPLATE)
	public ResponseEntity<?> detachChannel(@PathVariable(Navigation.TEMPLATE_ID) long templateId, @PathVariable(Navigation.CHANNEL_ID) long channelId) {

		templateService.detachChannel(templateId, channelId);
		return ResponseEntity.ok().build();
	}
}
