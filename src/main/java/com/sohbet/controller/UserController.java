package com.sohbet.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.mapper.UserMapper;
import com.sohbet.request.AdminUserUpdateRequest;
import com.sohbet.request.UpdatePasswordRequest;
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private  UserService userService;
    
    private  UserMapper userMapper;
    
    @Autowired
    public UserController(UserService userService,UserMapper userMapper) {
		this.userService=userService;
		this.userMapper=userMapper;
	}
    
	
//--------------- ger user by id--------------------------
	@GetMapping( "/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserById( @PathVariable Long id   ) {
		  UserDTO userDTO  = userService.getUserById(id);
		  
		  return ResponseEntity.ok(userDTO);
		
	}

	//Update Password
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public  ResponseEntity<ChatResponse> updatePassword( @Validated @RequestBody  UpdatePasswordRequest updatePasswordRequest) {
		userService.updatePassword(updatePasswordRequest);
		 ChatResponse response = new ChatResponse(ResponseMessage.USER_UPDATED_MESSAGE, true);
		 return ResponseEntity.ok(response) ;
		 
	}
	
	
    //--------------get all users pageable------------------
	@GetMapping("/auth/pages")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserDTO>> getAllProductWithpage(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
	                                                           @RequestParam(value = "size", required = false, defaultValue = "20") int size,
	                                                           @RequestParam(value = "sort", required = false, defaultValue = "create_at") String prop,
	                                                           @RequestParam(value = "type", required = false, defaultValue = "DESC") Direction direction){
Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

		
		Page<UserDTO> userDTOPage = userService.getAllPageable(pageable);
		

		return ResponseEntity.ok(userDTOPage);
		
		
	}
	
	//---------------------- get user ----------------------------
	@GetMapping
	@PreAuthorize( "hasRole('ADMIN') or hasRole('CUSTOMER')  " )
	public ResponseEntity<UserDTO> getUser() {
		   UserDTO userDTO =  userService.getPrincipal();
		   
		   return ResponseEntity.ok(userDTO);
		   
	}
	
	//---------------------- get all users -------------------------
	@GetMapping("/auth/all")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDTO>>getAllUser(){
   
		List<UserDTO> usersDTO = userService.getAllUsers();
		
		return ResponseEntity.ok(usersDTO);
		
	
		
	}	
//	@PutMapping("/admin/auth")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<ChatResponse> upDateUser(@Validated @RequestParam("imageId") String imageId, @RequestBody UpdateUserRequest userRequest){
//		
//		  userService.updateUser(imageId,userRequest);
//		 ChatResponse response = new ChatResponse(ResponseMessage.USER_UPDATED_MESSAGE, true);
//		 
//		 return ResponseEntity.ok(response);
//		
//	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ChatResponse> deleteUser(@PathVariable Long id){
		
		userService.deleteUserWithId(id);
		ChatResponse response = new ChatResponse();
		 response.setMessage(ResponseMessage.USER_DELETED);
		 response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
		
	
	}
	
//	// Admin herhangi bir kullanıcıyı update etsin
//		@PutMapping("/{id}/auth")
//		@PreAuthorize("hasRole('ADMIN')")
//		public ResponseEntity<ChatResponse> updateUserAuth( @PathVariable Long id ,  
//				  @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest ) {
//			
//			 userService.updateUserAuth(id,adminUserUpdateRequest);
//			 
//			 ChatResponse response = new ChatResponse();
//				response.setMessage(ResponseMessage.USER_UPDATED_MESSAGE);
//				response.setSuccess(true);
//				
//				return ResponseEntity.ok(response);
//			
//			
//		}
//	@GetMapping("/profile")
//	public ResponseEntity<UserDTO>getUserProfile(@RequestHeader ("Authorization") String token){
//		
//	UserDTO userDTO=	userService.findUserProfile(token);
//	 return ResponseEntity.ok(userDTO);
//		
//		
//		
//		
//	}
//	
//	@GetMapping("/{query}")
//	public ResponseEntity<List<UserDTO>> searchUser (@PathVariable("query")String query) {
//		
//	List<UserDTO> userDTOs=	userService.searchUsers(query);
//	
//	return ResponseEntity.ok(userDTOs);
//		
//	}
	
	//------- ikinci update token ile olacakti yarim biraktin----------------
//	@PutMapping("/update")
//	public ResponseEntity<ChatResponse> updateUserHandler(@RequestBody UpdateUserRequest userRequest,@RequestHeader ("Authorization") String token){
//		UserDTO userDTO=	userService.findUserProfile(token);
//	User user=	userMapper.userDTOToUser(userDTO);
//		
//		userService.updateuser(user);
//	}
	
	
}



