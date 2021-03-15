package com.asikorsky.m3u.web;

public interface Navigation {

	String ROOT = "/";
	String INDEX = "/index";
	String INDEX_HTML = "index.html";
	String[] STATIC_RESOURCES = {"/img/**", "/css/**", "/js/**"};

	String ID = "id";
	String ID_PATH_PATH_PARAM = "{" + ID + "}";
	String PLAYLIST_ID = "playlistId";
	String PLAYLIST_ID_PATH_PARAM = "{" + PLAYLIST_ID + "}";
	String CATEGORY_ID = "categoryId";
	String CATEGORY_ID_PATH_PARAM = "{" + CATEGORY_ID + "}";
	String TEMPLATE_ID = "templateId";
	String TEMPLATE_ID_PATH_PARAM = "{" + TEMPLATE_ID + "}";
	String CHANNEL_ID = "channelId";
	String CHANNEL_ID_PATH_PARAM = "{" + CHANNEL_ID + "}";
	String DOWNLOAD = "download";
	String DOWNLOAD_PATH_PARAM = "{" + DOWNLOAD + "}";

	String FILE = "file";

	String PLAYLIST = "/playlist";
	String CATEGORY = "/category";
	String CHANNEL = "/channel";
	String M3U = "/m3u";
	String TEMPLATE = "/template";

	String ALL = "/all";
	String FILTER = "/filter";
	String UPLOAD = "/upload";
	String DOWNLOAD_FOR_PLAYLIST = "/download/playlist/" + PLAYLIST_ID_PATH_PARAM;
	String LOAD_FOR_PLAYLIST = "/playlist/" + PLAYLIST_ID_PATH_PARAM;
	String BY_ID = "/" + ID_PATH_PATH_PARAM;
	String FROM_PLAYLIST = "/from/playlist/" + PLAYLIST_ID_PATH_PARAM;
	String CHECK_PLAYLIST_CHANNELS_FOR_TEMPLATE = "/check-channels/" + PLAYLIST_ID_PATH_PARAM + "/" + TEMPLATE_ID_PATH_PARAM;
	String DETACH_CHANNEL_FROM_CATEGORY = "/detach/" + CATEGORY_ID_PATH_PARAM + "/" + CHANNEL_ID_PATH_PARAM;
	String ATTACH_CHANNEL_TO_CATEGORY = "/attach/" + CATEGORY_ID_PATH_PARAM + "/" + CHANNEL_ID_PATH_PARAM;
	String DETACH_CHANNEL_FROM_TEMPLATE = "/detach/" + TEMPLATE_ID_PATH_PARAM + "/" + CHANNEL_ID_PATH_PARAM;
	String ATTACH_CHANNEL_TO_TEMPLATE = "/attach/" + TEMPLATE_ID_PATH_PARAM + "/" + CHANNEL_ID_PATH_PARAM;
	String DETACH_CHANNEL_FROM_PLAYLIST = "/detach/channel/" + PLAYLIST_ID_PATH_PARAM + "/" + CHANNEL_ID_PATH_PARAM;
	String VALIDATE_BY_ID = "/validate" + BY_ID + "/" + DOWNLOAD_PATH_PARAM;
}
