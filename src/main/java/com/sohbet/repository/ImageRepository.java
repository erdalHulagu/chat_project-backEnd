package com.sohbet.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sohbet.domain.Image;
import com.sohbet.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ImageRepository extends JpaRepository<Image,String> {
	

	@EntityGraph(attributePaths = "id") // parametreye id değeri girildiği zaman, aynı seviyedeki datalar gelir, 
	//bağlı olduğu imageData lar gelmemiş olacak
	List<Image> findAll();
	
	@EntityGraph(attributePaths = "id") // imageFile ile ilgili datalar gelsin
	Optional<Image> findImageById(String Id);

	 @EntityGraph(attributePaths = "imageData")
	    Optional<Image> findById(String id);

	

//	 @Query("SELECT i FROM Image i WHERE i.id = :profileImage")
//	    Image getUserImageByUserId(@Param("profileImage") String profileImage);
////
//	@Query("SELECT i FROM Image i JOIN i.user u WHERE u.profileImageId = : i.id ")
//	Image getImageByUserIdAndImageId(User user);

	

}
	


