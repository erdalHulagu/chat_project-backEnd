package com.sohbet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sohbet.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    @EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);

//	@EntityGraph(attributePaths = "image")
	Optional<User>  findById(Long id);

	@EntityGraph(attributePaths = "roles")
	Page<User> findAll(Pageable pageable);
	
	
	Boolean existsByEmail(String email);

	@Query("Select u from User u join u.image im where im.id=:id")
	List<User> findUserByImageId(@Param("id") String id);
	
//	@Query("SELECT COUNT(*) FROM User u JOIN u.image img WHERE img.id = :id")
//	Integer findUserCountByImageId(@Param("id") String id);
	
	
//	@Query( "SELECT count(*) from User u join u.image img where img.id=:id")
//	Integer findUserCountByImageId(String id);

//	@Modifying // JpaRepository içinde custom bir query ile DML operasyonları yapılıyor ise  @Modifying konulur
//	   @Query( "UPDATE User u SET u.firstName=:firstName, u.lastName=:lastName, u.address=:address, u.image=:image, u.phoneNumber=:phoneNumber, u.email=:email, u.createAt=:createAt, u.updateAt=:updateAt WHERE u.id=:id" ) 
//	   		
//	User update(@Param("id") Long id,
//			   	@Param("firstName") String firstName,
//			   	@Param("lastName") String lastName,
//			   	@Param("address") String address,
//			   	@Param("image") Set<String> image,
//			   	@Param("phoneNumber") String phoneNumber,
//			   	@Param("email") String email,
//			   	@Param("createAt")  LocalDateTime createAt,
//			   	@Param("updateAt")  LocalDateTime updateAt
//			   	);
//	@Modifying
//	@Query("SELECT u FROM User u WHERE u.firstName LIKE CONCAT('%', :query, '%') OR u.email LIKE CONCAT('%', :query, '%')")
//	public List<User>searchUser(@Param("query") String query);
}

