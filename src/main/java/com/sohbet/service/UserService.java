package com.sohbet.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.User;
import com.sohbet.exception.BadRequestException;
import com.sohbet.exception.ConflictException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.UserRepository;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;
import com.sohbet.security.SecurityUtils;
import com.sohbet.domain.Role;
import com.sohbet.enums.RoleType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	
	private final ImageService imageService;
	
	private final RoleService roleService;
	
	private final PasswordEncoder passwordEncoder;
	
	
	
	
	//---------- get current user ----------------------
	public UserDTO getPrincipal() {
		 User currentUser =  getCurrentUser();
		  UserDTO userDTO = userMapper.userToUserDto(currentUser);
		  return userDTO;
	
		
	}
	
	public User getCurrentUser() {
		
		String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
		 new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		UserDTO userDTO =  getUserByEmail(email);
	User user=userMapper.userDTOToUser(userDTO);
		return user ;
	}
	
		
		
//--------------- get user by id ----------------
		public UserDTO getUserById(Long id) {
			
		User user =	userRepository.findById(id).orElseThrow(()-> new  ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE,id)));
			
	UserDTO userDTO =	userMapper.userToUserDto(user);
			return userDTO;
			
			
			
		}

		
	// --------------- get all user by list -------------------
		public List<UserDTO> getAllUsers() {
			List<User> userList = userRepository.findAll();
			
			if (userList.isEmpty()) {
				new ResourceNotFoundException(String.format(ErrorMessage.USER_LIST_IS_EMPTY));
			}
		List <UserDTO> userDTOList	=userMapper.userToUserDTOList(userList);
			return userDTOList;
		}
		public void saveUser(RegisterRequest registerRequest) {
			if(userRepository.existByEmail(registerRequest.getEmail())) {
				throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
			}
			
			Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
			
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			
			String encodedPassword =  passwordEncoder.encode(registerRequest.getPassword());
			
			User user = new User();
			
			user.setFirstName(registerRequest.getFirstName());
			user.setLastName(registerRequest.getLastName());
			user.setEmail(registerRequest.getEmail());
			user.setPassword(encodedPassword);
			user.setPhoneNumber(registerRequest.getPhoneNumber());
			user.setAddress(registerRequest.getAddress());
			user.setRoles(roles);
			
			userRepository.save(user);
		}
		
	//------------------ save user ------------------------
		public void saveUser(RegisterRequest registerRequest, String imageId) {
			if(userRepository.existByEmail(registerRequest.getEmail())) {
				throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
			}
               Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
			
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			
			String encodedPassword =  passwordEncoder.encode(registerRequest.getPassword());
			
			User user = new User();
			
			Image imageFile =getImage(imageId);
//			Integer usedUserImageCount= userRepository.findCountingById(imageFile);
//				
//			if (usedUserImageCount > 0) {
//				throw new ResourceNotFoundException(ErrorMessage.IMAGE_USED_MESSAGE);
//			}
			
			user.setFirstName(registerRequest.getFirstName());
			user.setLastName(registerRequest.getLastName());
			user.setEmail(registerRequest.getEmail());
			user.setPassword(encodedPassword);
			user.setPhoneNumber(registerRequest.getPhoneNumber());
			user.setAddress(registerRequest.getAddress());
			user.setRoles(roles);
			
		
			Set<Image> image = new HashSet<>();
	     	image.add(imageFile);
			
			user.setImage(image);
			
			userRepository.save(user);
			
		}
		
		//----------------------- update user ----------------------
		public UserDTO updateUser(String imageId, UserRequest userRequest) {

	User user=userMapper.userRequestToUser(userRequest);
			

	       if ((user==null)) {
	    		new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_IS_NOT_MATCH));
		}
	       Image imageFile =getImage(imageId);
	   
	      
	        Set<Image> image = new HashSet<>();
	        image.add(imageFile);
	        user.setImage(image);
	       
		 userRepository.save(user);
		         
		   UserDTO userDTO =  userMapper.userToUserDto(user);
		
		   return userDTO;
		}

		//------------- delete user -----------------
		public void deleteUserWithId(Long id) {
			UserDTO userDto= getUserById(id);	
		User user=	userMapper.userDTOToUser(userDto);
			if (user.getBuiltIn()){
				new  BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
				
			}
			userRepository.deleteById(id);
			
		}

	// ------------- find user by email ---------------
		public UserDTO getUserByEmail(String email) {
			
	        User	user =	(userRepository.findByEmail
			                            (email).orElseThrow(
			                                  ()-> new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_NOT_FOUND))));
			
		UserDTO userDTO =	userMapper.userToUserDto(user);

	return userDTO;
		}

		
// ------------ find emage with emage id --------------		
	public Image getImage (String id) {
		Image imageFile =imageService.findImageByImageId(id);
		return imageFile;
	}

	//------------- get all user pegeable ----------------------
	public Page<UserDTO> getAllPageable(Pageable pageable) {
		   Page<User> userPage = userRepository.findAll(pageable);
		   
	        return getUserDTOPage(userPage);
	
	}
	


	private Page<UserDTO> getUserDTOPage(Page<User> userPage) {
		
		 Page<UserDTO> userDTOPage =  userPage.map(new Function<User, UserDTO>() {
			 @Override
			public UserDTO apply(User user) {
				
				return userMapper.userToUserDto(user);
			}
		});
		 
		 return userDTOPage;
		
	}
		
		
}
		

