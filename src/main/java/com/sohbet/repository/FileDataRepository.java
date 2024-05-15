package com.sohbet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sohbet.domain.ImageData;


public interface FileDataRepository extends JpaRepository<ImageData, Long>{
	Optional<ImageData> findById(Long id);
}
