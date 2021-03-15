package com.asikorsky.m3u.domain.repository;

import com.asikorsky.m3u.domain.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Long> {

	Optional<TemplateEntity> findByName(String name);
}
