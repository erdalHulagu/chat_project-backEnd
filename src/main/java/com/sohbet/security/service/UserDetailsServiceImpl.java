package com.sohbet.security.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sohbet.domain.User;
import com.sohbet.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
		
		 User user =  userService.getUserByEmail(email);
		 return UserDetailsImpl.build(user);
	}

}
