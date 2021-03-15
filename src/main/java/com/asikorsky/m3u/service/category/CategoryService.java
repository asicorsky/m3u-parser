package com.asikorsky.m3u.service.category;

import com.asikorsky.m3u.dto.CategoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {

	List<CategoryDto> getForPlaylist(long playlistId);

	void detachChannel(long categoryId, long channelId);

	void attachChannel(long categoryId, long channelId);
}
