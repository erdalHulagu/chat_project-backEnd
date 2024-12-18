package com.sohbet.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.service.ImageService;
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
	
	@Autowired
	private ImageService imageService;
	
	// ------------ get user by user id -------------------
	@Transactional
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {

		User user = userService.getUserById(userId);

		UserDTO userDTO = userMapper.userToUserDto(user);
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
	@Transactional
	@PutMapping("/auth/{imageId}")
//	@PreAuthorize("hasRole('ADMIN') or hasRole('ANONYMOUS')")
    public ResponseEntity<UserDTO> upDateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,@PathVariable String imageId) {
        UserDTO userDTO = userService.updateUser(updateUserRequest,imageId);
    
        return ResponseEntity.ok(userDTO);
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

		UserDTO userDTO = userService.findUserByImageId(imageId);

		return ResponseEntity.ok(userDTO);

	}
	
	//------------add user as friend -------------
	@PatchMapping("/addFriend/{friendId}")
	public ResponseEntity<Response> addUserAsFriend(@PathVariable Long friendId) {

		UserDTO userDTO=userService.findUserProfile();
		User currentUser= userMapper.userDTOToUser(userDTO);
		userService.addUserAsFriend(currentUser,friendId);
		Response response = new Response();
		response.setMessage(ResponseMessage.FRIEND_ADDED);

		return ResponseEntity.ok(response);
		
		
	}
	@PatchMapping("/addImage/{imageId}")
	public ResponseEntity<Response> addImageToYourImages(@PathVariable String imageId) {
		
		UserDTO userDTO=userService.findUserProfile();
		User currentUser= userMapper.userDTOToUser(userDTO);
		userService.addImageToYourImages(currentUser,imageId);
		Response response = new Response();
		response.setMessage(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE);
		
		return ResponseEntity.ok(response);
		
		
	}
	//-------------- getCurrentUsersImages-------------
	@GetMapping("/myImages")
	public Set<Image> getUsersImages() {
	    UserDTO userDTO = userService.findUserProfile();
	    User user2 = userMapper.userDTOToUser(userDTO);
	User user=	userService.getUserById(user2.getId());
	
	    
	    System.out.println("User ID: " + user.getId());
	    if (user.getMyImages().isEmpty()) {
	        System.out.println("No images found for the user");
	        return new HashSet<>(); 
	    }
	    
	 Set<Image>imgsImages= user.getMyImages().stream()
			    .map(img -> imageService.getImageById(img.getId()))
			    .collect(Collectors.toSet());
	 return imgsImages;
	}
}
