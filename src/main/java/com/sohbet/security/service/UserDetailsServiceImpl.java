package com.sohbet.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sohbet.domain.User;
import com.sohbet.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Autowired
	UserDetailsImpl userDetailsImpl;
	
	
	@Override
	public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
		
		 User user =  userService.getUserByEmail(email);
		 
		 return userDetailsImpl.build(user); //return UserDetailsImpl.build(user) statik idi biz onu normal yaptik
	}

}
