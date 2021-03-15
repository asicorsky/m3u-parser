package com.asikorsky.m3u.domain.repository;

import com.asikorsky.m3u.domain.entity.ChannelEntity;
import com.asikorsky.m3u.domain.entity.QChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long>, QuerydslPredicateExecutor<ChannelEntity>, QuerydslBinderCustomizer<QChannelEntity> {

	@Query("select distinct channel from PlaylistEntity playlist join playlist.categories category join category.channels channel where playlist.id = :playlistId")
	List<ChannelEntity> getForPlaylist(@Param("playlistId") long playlistId);

	@Query("select distinct channel from TemplateEntity template join template.channels channel where template.id = :templateId")
	List<ChannelEntity> getForTemplate(@Param("templateId") long templateId);

	Optional<ChannelEntity> findByName(String name);

	@Override
	default void customize(QuerydslBindings querydslBindings, QChannelEntity qChannelEntity) {

	}

}
