package com.sohbet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sohbet.domain.Role;
import com.sohbet.enums.RoleType;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByType(RoleType type);

}
