package com.asikorsky.m3u.domain.repository;

import com.asikorsky.m3u.domain.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

	Optional<PlaylistEntity> findByName(String name);

	boolean existsByName(String name);

	void deleteByName(String name);
}
