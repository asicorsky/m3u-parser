package com.asikorsky.m3u.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "templates")
@Entity
public class TemplateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany
	@JoinTable(name = "templates_to_channels", joinColumns = @JoinColumn(name = "template_id"), inverseJoinColumns = @JoinColumn(name = "channel_id"))
	private List<ChannelEntity> channels = new ArrayList<>();

	public TemplateEntity() {

	}

	public TemplateEntity(String name) {

		this.name = name;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
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
