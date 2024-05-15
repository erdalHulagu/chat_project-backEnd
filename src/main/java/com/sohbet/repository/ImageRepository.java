package com.sohbet.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sohbet.domain.Image;

import java.util.List;
import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image,String> {
	

	@EntityGraph(attributePaths = "id") // parametreye id değeri girildiği zaman, aynı seviyedeki datalar gelir, 
	//bağlı olduğu imageData lar gelmemiş olacak
	List<Image> findAll();
	
	@EntityGraph(attributePaths = "id") // imageFile ile ilgili datalar gelsin
	Optional<Image> findImageById(String Id);

	

}
	


