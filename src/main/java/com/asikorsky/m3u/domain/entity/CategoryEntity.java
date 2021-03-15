package com.asikorsky.m3u.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "categories")
@Entity
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "playlist_id")
	private PlaylistEntity playlist;

	private String name;

	@ManyToMany
	@JoinTable(name = "categories_to_channels", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "channel_id"))
	private List<ChannelEntity> channels = new ArrayList<>();

	public CategoryEntity() {

	}

	public CategoryEntity(PlaylistEntity playlist, String name) {

		this.playlist = playlist;
		this.name = name;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public PlaylistEntity getPlaylist() {

		return playlist;
	}

	public void setPlaylist(PlaylistEntity playlist) {

		this.playlist = playlist;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public List<ChannelEntity> getChannels() {

		return channels;
	}

	public void setChannels(List<ChannelEntity> channels) {

		this.channels = channels;
	}
}
