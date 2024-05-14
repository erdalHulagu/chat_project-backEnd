package com.sohbet.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sohbet.domain.Image;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image,String> {
	

	
	@EntityGraph(attributePaths = "id") 
	Optional<Image> findImageById(String id);


	@EntityGraph(attributePaths = "id") 
	Optional<Image> findById(String id);

//	@EntityGraph(attributePaths = "image")
//	void delete(byte[] imageData);



	
}

