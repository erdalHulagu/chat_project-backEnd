package com.sohbet.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.Response;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.domain.Image;
import com.sohbet.request.UserRequest;
import com.sohbet.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
		
		UserDTO userDTO=userService. getUserById(id);
		
		return ResponseEntity.ok(userDTO);
	
	}
	public ResponseEntity<Response> saveUser(@PathVariable ("imageId") String imageId, @RequestBody UserRequest userRequest) {
		
		Response response = new Response();
		response.setMessage(ResponseMessage.USER_CREATED_SUCCESFULLY);
		
		userService.save(imageId, userRequest);
		
		return ResponseEntity.ok(response);
		
	}
	
	
	
	@GetMapping("/profile")
	public ResponseEntity<UserDTO> findUserProfile(){
		
	UserDTO userDTO=	userService.findUserProfile();
	 
	return ResponseEntity.ok(userDTO);

	}
	
//	@GetMapping("/{query}")
//	public ResponseEntity<List<UserDTO>> searchUser(@PathVariable ("query") String query ){
//		List<UserDTO> usersDtos=userService.seraachUser(query);
//		return ResponseEntity.ok(usersDtos);
//	}
	
//	@GetMapping
//	public ResponseEntity<Page<User>>getAllUser(
//			                                     @RequestParam(value = "page", required = false, defaultValue = "0") int page,
//		                                         @RequestParam(value = "size", required = false, defaultValue = "20") int size,
//				                                 @RequestParam(value = "type", required = false, defaultValue = "ASC") Direction direction
//				){
//     Pageable pageable = PageRequest.of(page, size, Sort.by(direction));
//
//		
//		Page<User> users = userService.getAll(pageable);
//		
//
//		return ResponseEntity.ok(users);
//		
//		
//		
//		
//		
//	}
	
	//get all users
	@GetMapping("/admin")
	public ResponseEntity<List<UserDTO>>getAllUser(){
   

		
		List<UserDTO> usersDTO = userService.getAllUsers();
		

		return ResponseEntity.ok(usersDTO);
		
	}

	@PutMapping("/{imageId}")
	
	public ResponseEntity<UserDTO> upDateUser(@Validated @PathVariable String imageId, @RequestBody UserRequest userRequest){
		
		 UserDTO updateduser = userService.updateUser(imageId,userRequest);
		 Response response = new Response();
		 response.setMessage(ResponseMessage.USER_UPDATED_MESSAGE);
		 return ResponseEntity.ok(updateduser);
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable Long id){
		
		userService.deleteUserWithId(id);
		Response response = new Response();
		 response.setMessage(ResponseMessage.USER_DELETED);
		
		return ResponseEntity.ok(response);
		
		
	
	}
//	@GetMapping("/email")
//	public ResponseEntity<UserDTO> getUserByEmailEntity (@RequestBody String email){
//		
//	UserDTO  emaillUser = userService.getUserByEmail(email);
//	
//	return ResponseEntity.ok(emaillUser);
//	
//		
//		
//		
//		}
	
	
}


















