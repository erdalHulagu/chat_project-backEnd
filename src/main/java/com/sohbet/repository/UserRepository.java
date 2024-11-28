package com.sohbet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sohbet.domain.Image;
import com.sohbet.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);

	@EntityGraph(attributePaths = "roles")
	Page<User> findAll(Pageable pageable);

	Boolean existsByEmail(String email);

	 @Query("SELECT u FROM User u JOIN u.profileImage i WHERE i.id = :imageId")
	Optional<User> findUserByImageId(@Param("imageId") String imageId);
	
	@Query("Select u from User u join u.profileImage.id pId where pId=:imageId")
	List<User> findUseListByImageId(@Param("imageId") String string);

	@Query("SELECT count(*) from User u join u.profileImage.id pId where pId=:id")
	Integer findUserCountByImageId(@Param("id") String id);

	@EntityGraph(attributePaths = "profileImage")
	Optional<User> findUserById(Long id);

	@Query("SELECT u FROM User u WHERE u.firstName = :firstName")
	List<User> searchUsersByUserName(@Param("firstName") String firstName);

//	@Query("SELECT u FROM User u join u.profileImage pimg where pimg.id= : profileImage.id")
//	Long getImage(@Param("id")String id);

//@Query("SELECT u FROM User u join u.profileImage pimg where pimg.id= : profileImage.id")
//	Long getImage(@Param("profileImage")Image profileImage);

//	@Query("SELECT COUNT(*) FROM User u JOIN u.image img WHERE img.id = :id")
//	Integer findUserCountByImageId(@Param("id") String id);

//	@Modifying
//	@Query("SELECT u FROM User u WHERE u.firstName LIKE CONCAT('%', :query, '%') OR u.email LIKE CONCAT('%', :query, '%')")
//	public List<User>searchUser(@Param("query") String query);
}
