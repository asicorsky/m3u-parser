package com.asikorsky.m3u.service.playlist;

import com.asikorsky.m3u.dto.ChannelDto;
import com.asikorsky.m3u.dto.PlaylistDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PlaylistService {

	List<PlaylistDto> getPlaylists();

	void remove(long id);

	void detachChannel(long playlistId, long channelId);

	List<ChannelDto> validate(long id, boolean download);
}
