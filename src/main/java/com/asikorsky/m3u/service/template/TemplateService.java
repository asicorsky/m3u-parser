package com.asikorsky.m3u.service.template;

import com.asikorsky.m3u.dto.TemplateComparisonDto;
import com.asikorsky.m3u.dto.TemplateDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TemplateService {

	void createFromPlaylist(long playlistId, TemplateDto template);

	List<TemplateDto> getTemplates();

	TemplateComparisonDto checkChannels(long playlistId, long templateId);

	void attachChannel(long templateId, long channelId);

	void detachChannel(long templateId, long channelId);
}
