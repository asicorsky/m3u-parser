package com.asikorsky.m3u.service.channel;

import com.asikorsky.m3u.dto.ChannelDto;
import com.asikorsky.m3u.dto.ChannelFilter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ChannelService {

	List<ChannelDto> getChannels(ChannelFilter filter);

	List<ChannelDto> validate(ChannelFilter filter, boolean download);
}
