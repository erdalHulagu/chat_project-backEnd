package com.sohbet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sohbet.domain.Role;
import com.sohbet.enums.RoleType;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Role findByType(RoleType roleType) {
		Role role =  roleRepository.findByType(roleType).orElseThrow(()->
		       new ResourceNotFoundException(String.format(
		    		   ErrorMessage.ROLE_NOT_FOUND_MESSAGE, roleType.name())));
		
		return role ; 
		
		
		
	}

}
