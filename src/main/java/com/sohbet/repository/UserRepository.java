package com.sohbet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sohbet.domain.User;

public interface UserRepository extends JpaRepository<User, Long>  {

@EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);

	@EntityGraph(attributePaths = "image")
	Optional<User>  findById(Long id);

	@EntityGraph(attributePaths = { "image"})
	List<User> findAll();


	boolean existByEmail(String email);
}
