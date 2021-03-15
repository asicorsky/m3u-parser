package com.asikorsky.m3u.web.controller;

import com.asikorsky.m3u.service.category.CategoryService;
import com.asikorsky.m3u.web.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Navigation.CATEGORY)
public class CategoryController {

	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {

		this.categoryService = categoryService;
	}

	@GetMapping(Navigation.LOAD_FOR_PLAYLIST)
	public ResponseEntity<?> loadForPlaylist(@PathVariable(Navigation.PLAYLIST_ID) long playlistId) {

		var categories = categoryService.getForPlaylist(playlistId);
		return ResponseEntity.ok(categories);
	}

	@DeleteMapping(Navigation.DETACH_CHANNEL_FROM_CATEGORY)
	public ResponseEntity<?> detachChannel(@PathVariable(Navigation.CATEGORY_ID) long categoryId, @PathVariable(Navigation.CHANNEL_ID) long channelId) {

		categoryService.detachChannel(categoryId, channelId);
		return ResponseEntity.ok().build();
	}

	@PostMapping(Navigation.ATTACH_CHANNEL_TO_CATEGORY)
	public ResponseEntity<?> attachChannel(@PathVariable(Navigation.CATEGORY_ID) long categoryId, @PathVariable(Navigation.CHANNEL_ID) long channelId) {

		categoryService.attachChannel(categoryId, channelId);
		return ResponseEntity.ok(HttpEntity.EMPTY);
	}
}
