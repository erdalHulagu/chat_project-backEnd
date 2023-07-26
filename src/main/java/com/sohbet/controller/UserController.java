package com.sohbet.controller;


import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.request.UserRequest;
import com.sohbet.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
	
    
    
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
		
		UserDTO userDTO=userService. getUserById(id);
		
		return ResponseEntity.ok(userDTO);
	
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> getAllProductWithpage(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
	                                                           @RequestParam(value = "size", required = false, defaultValue = "20") int size,
	                                                           @RequestParam(value = "sort", required = false, defaultValue = "create_at") String prop,
	                                                           @RequestParam(value = "type", required = false, defaultValue = "DESC") Direction direction){
Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

		
		Page<UserDTO> userDTOPage = userService.getAllPageable(pageable);
		

		return ResponseEntity.ok(userDTOPage);
		
		
	}
	
	@GetMapping("/admin")
	public ResponseEntity<List<UserDTO>>getAllUser(){
   
		List<UserDTO> usersDTO = userService.getAllUsers();
		
		return ResponseEntity.ok(usersDTO);
		
	
		
	}
	@PutMapping("/{imageId}")
	
	public ResponseEntity<UserDTO> upDateUser(@Validated @PathVariable String imageId, @RequestBody UserRequest userRequest){
		
		 UserDTO updateduser = userService.updateUser(imageId,userRequest);
		 ChatResponse response = new ChatResponse();
		 response.setMessage(ResponseMessage.USER_UPDATED_MESSAGE);
		 return ResponseEntity.ok(updateduser);
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ChatResponse> deleteUser(@PathVariable Long id){
		
		userService.deleteUserWithId(id);
		ChatResponse response = new ChatResponse();
		 response.setMessage(ResponseMessage.USER_DELETED);
		
		return ResponseEntity.ok(response);
		
		
	
	}
	
	
	
}



