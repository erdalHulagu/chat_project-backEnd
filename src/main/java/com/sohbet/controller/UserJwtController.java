package com.sohbet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTOresponse.Response;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.request.LoginRequest;
import com.sohbet.request.RegisterRequest;
import com.sohbet.security.jwt.JwtService;
import com.sohbet.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class UserJwtController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/register")
	@Transactional
	public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		userService.saveUser(registerRequest);

		Response response = new Response();
		response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
		response.setSuccess(true);

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@PostMapping("/login")
	public String authenticateToken(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(loginRequest.getEmail());
		} else {
			throw new UsernameNotFoundException(ErrorMessage.AUTHENTICATION_USER_NOT_FOUND_MESSAGE);
		}

	}

}
   
   
   
   
   
   
   
   
   
   
   
   
   
   

