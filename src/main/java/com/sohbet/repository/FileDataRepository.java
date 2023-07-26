package com.sohbet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sohbet.domain.FileData;


public interface FileDataRepository extends JpaRepository<FileData, Long>{
	Optional<FileData> findById(Long id);
}
