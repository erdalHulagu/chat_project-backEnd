package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.exception.BadRequestException;
import com.sohbet.exception.ConflictException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.UserRepository;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;
import com.sohbet.security.config.SecurityUtils;
import com.sohbet.security.jwt.JwtUtils;


@Service
public class UserService {
	
	private UserRepository userRepository;
	
	private ImageService imageService;
	
	private UserMapper userMapper;
	
	private RoleService roleService;
		
	private PasswordEncoder passwordEncoder;
	
	private JwtUtils jwtUtils;
	
	@Autowired
	public UserService(UserRepository userRepository
			           ,ImageService imageService
			           ,UserMapper userMapper
			           ,RoleService roleService
			           ,PasswordEncoder passwordEncoder
			           ,JwtUtils jwtUtils) {
		
		this.userRepository=userRepository;
		this.imageService=imageService;
		this.userMapper=userMapper;
		this.roleService=roleService;
		this.passwordEncoder=passwordEncoder;
		this.jwtUtils=jwtUtils;
		
		
	}
	
	
	
	
	
	

	
	//------------------  get current user ------------------------
public User getCurrentUser() {
		
		String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
		 new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		User user =  getUserByEmail(email);
		return user ;
		
	}
 //------------------  get current userDTO ------------------------
   public UserDTO getPrincipal() {
		 User currentUser =  getCurrentUser();
		  // return userMapper.userToUserDTO(currentUser);
		  UserDTO userDTO = userMapper.userToUserDto(currentUser);
		  return userDTO;
	
		
	}

	
	
	
// -------------------  get user by id --------------
	public UserDTO getUserById(Long id) {
		
	User user =	userRepository.findById(id).orElseThrow(()-> new  ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE,id)));
		
UserDTO userDTO =	userMapper.userToUserDto(user);
		return userDTO;
		
		
		
	}
//
//	public Page<User> getAll(Pageable pageable) {
//
//		Page<User> userPage = userRepository.findAll(pageable);
//		
//		
//		return userPage;
//	}
	//--------------------get user profile------------------------
	public UserDTO findUserProfile() {
		
	
     User user=	getCurrentUser();
     
     if (user==null) {
    	 throw new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, user));
		
	}
   UserDTO userDTO=  userMapper.userToUserDto(user);
     return userDTO ;
	
		
		
	}
//	//--------------------sarch users from list of users with query-----------------------
//	
//	public List<UserDTO> seraachUser(String query){
//		List<User> user=userRepository.searchUser(query);
//		List<UserDTO> userDTOs=userMapper.userToUserDTOList(user);
//		return userDTOs;
//	}
	
	
//--------------------get all users------------------------
	public List<UserDTO> getAllUsers() {
		List<User> userList = userRepository.findAll();
		
		if (userList.isEmpty()) {
			new ResourceNotFoundException(String.format(ErrorMessage.USER_LIST_IS_EMPTY));
		}
	List <UserDTO> userDTOList	=userMapper.userToUserDTOList(userList);
		return userDTOList;
	}
	
	

	//update user
	public UserDTO updateUser(String imageId, UserRequest userRequest) {

User user=userMapper.userRequestToUser(userRequest);



       if ((user==null)) {
    		new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_IS_NOT_MATCH));
    		
	}
       Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);

       Set<Role> roles = new HashSet<>();
       roles.add(role);
       user.setRoles(roles);
       byte[] imgByt= imageService.getImage(imageId);
       
       Image img = new Image();
       img.setData(imgByt);

//   	   Integer imageCountCheck = userRepository.findUserCountByImageId(img.getId());
//
//   	   if (imageCountCheck > 0) {
//   		throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
//   	  }
   		
   		Set<Image> image=new HashSet<>();
   		
   		image.add(img);
       
	 userRepository.save(user);
	         
	   UserDTO userDTO =  userMapper.userToUserDto(user);
	
	   return userDTO;
	}
//	private Optional<User> getUserByEmail(String email) {
//		
//
//		  User user  =  userRepository.findByEmail(email).orElseThrow(()->
//		  			new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email))
//				);
//		return Optional.of((user)) ;
//		
//	}

	//---------------- register user----------------------
	public void saveUser(String imageId,RegisterRequest registerRequest) {
		if(userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
		}
		
	byte[] imgByt= imageService.getImage(imageId);
	Image img = new Image();
	img.setData(imgByt);

//	Integer imageCountCheck = userRepository.findUserCountByImageId(img.getId());
//
//	if (imageCountCheck > 0) {
//		throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
//	}
		
		Set<Image> image=new HashSet<>();
		
		image.add(img);
		
		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);
		
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		
		String encodedPassword =  passwordEncoder.encode(registerRequest.getPassword());

		

		User user = new User();
		user.setProfileImage(image);
		user.setRoles(roles);
		user.setPassword(encodedPassword);
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setAddress(registerRequest.getAddress());
//		user.setPhone(registerRequest.getPhoneNumber());
		user.setCreateAt(LocalDateTime.now());
		
	
		userRepository.save(user);
		
	}
	
	

	public void deleteUserWithId(Long id) {
		
		userRepository.deleteById(id);
		
	}

//------------- find user by email-------------------
	public User getUserByEmail(String email ) {
		
		  User user  =  userRepository.findByEmail(email).orElseThrow(()->
		  			new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email))
				);
		return user ;
	}







//
//	public void save(String imageId, UserRequest userRequest) {
//		byte[] imgByt= imageService.getImage(imageId);
//		Image img = new Image();
//		img.setData(imgByt);
//
////		Integer imageCountCheck = userRepository.findUserCountByImageId(img.getId());
//	//
////		if (imageCountCheck > 0) {
////			throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
////		}
//			
//			Set<Image> image=new HashSet<>();
//			
//			image.add(img);
//			
//			Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);
//			
//			Set<Role> roles = new HashSet<>();
//			roles.add(role);
//			
//
//			
//
//			User user = new User();
//			user.setProfileImage(image);
//			user.setRoles(roles);
//			user.setFirstName(userRequest.getFirstName());
//			user.setLastName(userRequest.getLastName());
//			user.setEmail(userRequest.getEmail());
//			user.setAddress(userRequest.getAddress());
////			user.setPhone(registerRequest.getPhoneNumber());
//			user.setCreateAt(LocalDateTime.now());
//			
//		
//			userRepository.save(user);
//		
//	}








	
	//------------ get image by string id ------------------  extra
//public Image getImage (String id) {
//	Image imageFile =imageService.findImageByImageId(id);
//	return imageFile;
//}
//	
	}
	


