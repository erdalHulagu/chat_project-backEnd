package com.sohbet.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.User;
import com.sohbet.mapper.UserMapper;
import com.sohbet.service.UserService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserService userService;
	
	private final UserMapper userMapper;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
		
		 UserDTO userDTO =  userService.getUserByEmail(email);
		 
	   User user= userMapper.userDTOToUser(userDTO);
		 return UserDetailsImpl.build(user);
	}

}
