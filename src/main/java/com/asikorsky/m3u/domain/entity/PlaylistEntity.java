package com.asikorsky.m3u.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "playlists")
@Entity
public class PlaylistEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "playlist")
	private List<CategoryEntity> categories;

	public PlaylistEntity() {

	}

	public PlaylistEntity(String name) {

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

	public List<CategoryEntity> getCategories() {

		return categories;
	}

	public void setCategories(List<CategoryEntity> categories) {

		this.categories = categories;
	}
}
