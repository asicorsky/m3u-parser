package com.asikorsky.m3u.domain.repository;

import com.asikorsky.m3u.domain.entity.CategoryEntity;
import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	Optional<CategoryEntity> findByPlaylistAndName(PlaylistEntity playlist, String name);

	List<CategoryEntity> findByPlaylist(PlaylistEntity playlist);
}
