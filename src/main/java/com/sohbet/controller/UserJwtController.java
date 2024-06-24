package com.sohbet.controller;



import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sohbet.DTOresponse.LoginResponse;
import com.sohbet.DTOresponse.Response;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.request.LoginRequest;
import com.sohbet.request.RegisterRequest;
import com.sohbet.security.jwt.JwtUtils;
import com.sohbet.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
public class UserJwtController {
	
	
//	private static final Logger logger = LoggerFactory.getLogger(UserJwtController.class);
	// Bu classımda sadece login ve register işlemleri yapılacak
	
   @Autowired
   private UserService userService;
   
   @Autowired
   private  AuthenticationManager authenticationManager;
   
   @Autowired
   private JwtUtils  jwtUtils;
   
   
   // --------------------register user---------------------
  
   @PostMapping("/register")
   @Transactional
   public ResponseEntity<Response> registerUser( @Valid @RequestBody RegisterRequest registerRequest  )  {
	   userService.saveUser(registerRequest);
	   
	   Response response = new Response();
	   response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
	   response.setSuccess(true);
	   
	   return new ResponseEntity<>(response,HttpStatus.CREATED);
  
   }
   
   
   
   // ------------------------login user-----------------------------
   @PostMapping("/login")
//   @Transactional
   public ResponseEntity<LoginResponse> authenticate( @Validated @RequestBody LoginRequest loginRequest    )  {
	   
	   UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
			     new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
	   
	    Authentication authentication  =  authenticationManager.
	    		       authenticate(usernamePasswordAuthenticationToken);
	    
	 UserDetails userDetails  =  (UserDetails) authentication.getPrincipal() ;// mevcut giriş yapan kullanıcıyı getirir
	    
	   String jwtToken =   jwtUtils.generateJwtToken(userDetails);
	   
	   LoginResponse loginResponse = new LoginResponse(jwtToken);
	   
	   return new ResponseEntity<>(loginResponse,HttpStatus.OK);
	   
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
}
