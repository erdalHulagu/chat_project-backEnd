package com.sohbet.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.Response;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.domain.Image;
import com.sohbet.domain.User;
import com.sohbet.mapper.UserMapper;
import com.sohbet.request.AdminUserUpdateRequest;
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.request.UserRequest;
import com.sohbet.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	
	// ------------ get user by user id -------------------
	@Transactional
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {

		User user = userService.getUserById(userId);
		
		UserDTO userDTO=userMapper.userToUserDto(user);
		return ResponseEntity.ok(userDTO);

	}
	// ------------ get user profile -------------------
	@Transactional
	@GetMapping("/profile")
//	@PreAuthorize( "hasRole('ADMIN') or hasRole('ANONYMOUS')")
	public ResponseEntity<UserDTO> findUserProfile() {

		UserDTO userDTO = userService.findUserProfile();

		return ResponseEntity.ok(userDTO);

	}

//	@GetMapping("/{query}")
//	public ResponseEntity<List<UserDTO>> searchUser(@PathVariable ("query") String query ){
//		List<UserDTO> usersDtos=userService.seraachUser(query);
//		return ResponseEntity.ok(usersDtos);
//	}

	@GetMapping
	public ResponseEntity<Page<UserDTO>> getAllUser(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
			@RequestParam(value = "type", required = false, defaultValue = "ASC") Direction direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction));

		Page<UserDTO> users = userService.getAllByPage(pageable);

		return ResponseEntity.ok(users);

	}
	// ------------ get all user -------------------
	@Transactional
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN') ")
	public ResponseEntity<Set<UserDTO>> getAllUser() {

		Set<UserDTO> usersDTO = userService.getAllUsers();

		return ResponseEntity.ok(usersDTO);

	}
	// ------------ up date user -------------------
	@PutMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ANONYMOUS')")
	public ResponseEntity<Response> upDateUser(@RequestParam(value = "imageId", required = false) String imageId,
			@Valid @RequestBody UpdateUserRequest updateUserRequest) {

		userService.updateUser(imageId, updateUserRequest);

		Response response = new Response();
		response.setMessage(ResponseMessage.USER_UPDATED_MESSAGE);
		return ResponseEntity.ok(response);

	}
	// ------------ delete user by id -------------------
	@DeleteMapping("{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable Long id) {

		userService.deleteUserById(id);
		Response response = new Response();
		response.setMessage(ResponseMessage.USER_DELETED);

		return ResponseEntity.ok(response);

	}
	// ------------ search user -------------------
	@GetMapping("/search")
	public ResponseEntity<Set<UserDTO>> searchUser(@Valid @RequestParam("firstName") String firstName) {

		List<User> users = userService.searchUserByName(firstName);

		Set<User> setUsers = new HashSet<>(users);

		Set<UserDTO> userDTOs = userMapper.mapUserListToUserDTOList(setUsers);

		return new ResponseEntity<>(userDTOs, HttpStatus.ACCEPTED);

	
	}

	// ------------ get user by image Id -------------------
	@GetMapping("/userImage/{imageId}")
	public ResponseEntity<UserDTO> getUserByImageId(@Valid @PathVariable String imageId) {
		
		UserDTO userDTO=userService.findUserByImageId(imageId);
		
		return ResponseEntity.ok(userDTO);
		
		
		
	}
}
