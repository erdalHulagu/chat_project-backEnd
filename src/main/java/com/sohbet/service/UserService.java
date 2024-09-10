package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.ImageData;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.exception.BadRequestException;
import com.sohbet.exception.ConflictException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.ImageRepository;
import com.sohbet.repository.UserRepository;
import com.sohbet.request.AdminUserUpdateRequest;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.security.config.SecurityUtils;

import ch.qos.logback.core.joran.conditional.IfAction;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private  ImageService imageService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ImageRepository imageRepository;

	
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);


	// ------------------ get current user ------------------------
	public User getCurrentUser() {

		String email = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		User user = getUserByEmail(email);
	
		return user;

	}

	// ------------------ get principal userDTO ------------------------
	public UserDTO getPrincipal() {
		User currentUser = getCurrentUser();
		UserDTO userDTO = userMapper.userToUserDto(currentUser);
		return userDTO;

	}

// -------------------  get userDTO by id --------------
	
	@Transactional
	public UserDTO getUserById(@PathVariable Long id) {

		User user = getUser(id);

		
		UserDTO userDTO = userMapper.userToUserDto(user);
		return userDTO;

	}

// -------------------  get user by id --------------
	
	public User getUser(Long id) {

		User user = userRepository.findUserById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, id)));

		return user;
	}

	// --------------------get user profile------------------------
	public UserDTO findUserProfile() {

		User user = getCurrentUser();
		

		if (user == null) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, user));

		}
		UserDTO userDTO = userMapper.userToUserDto(user);
		return userDTO;

	}

	public Page<UserDTO> getAllByPage(Pageable pageable) {
		Page<User> userPage = userRepository.findAll(pageable);

		return getUserDTOPage(userPage);

	}
	//--------------------get all users pageable------------------------
	private Page<UserDTO> getUserDTOPage(Page<User> userPage) {

		Page<UserDTO> usersDTOPage = userPage.map(new Function<User, UserDTO>() {
			
			
			
			@Override
			public UserDTO apply(User user) {

				return userMapper.userToUserDto(user);
			}
		});
		
		if (usersDTOPage.isEmpty()) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_LIST_IS_EMPTY));
		}

		return usersDTOPage;

	}

//--------------------get all users------------------------
	public List<UserDTO> getAllUsers() {
		List<User> userList = userRepository.findAll();

		if (userList.isEmpty()) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_LIST_IS_EMPTY));
		}
		List<UserDTO> userDTOList = userMapper.userToUserDTOList(userList);
		return userDTOList;
	}

	// ------------------update user---------------------
	public void updateUser(String imageId, UpdateUserRequest updateUserRequest) {
		User user = getCurrentUser();

		if ((user == null)) {
			new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE));

		}

		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
		}

		boolean emailExist = userRepository.existsByEmail(updateUserRequest.getEmail());

		if (emailExist && !updateUserRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, updateUserRequest.getEmail()));
		}

		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);

		Set<Role> roles = new HashSet<>();
		roles.add(role);

		Image image = imageService.getImageById(imageId);
		Set<Image> images = new HashSet<>();
		images.add(image);

		List<User> userList = userRepository.findUserByImageId(image.getId());

		for (User u : userList) {
			// bana gelen user Id si ile yukardakiList türündeki user Id leri eşit olmaları
			// lazım,
			// eğer eşit değilse girilenm image başka bir user için yüklenmiş
			if (user.getId().longValue() != u.getId().longValue()) {
				throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
			}

		}

		user.setProfileImage(image);
		user.setMyImages(images);
		user.setRoles(roles);
		user.setUpdateAt(LocalDateTime.now());
		user.setFirstName(updateUserRequest.getFirstName());
		user.setLastName(updateUserRequest.getLastName());
		user.setAddress(updateUserRequest.getAddress());
		user.setPhone(updateUserRequest.getPhone());
		user.setEmail(updateUserRequest.getEmail());

		userRepository.save(user);
	}

	// ---------------- register user----------------------
	
	public void saveUser(String imageId,RegisterRequest registerRequest) {

		Image profileImage = imageService.getImageById(imageId);
		
		Set<Image>images=new HashSet<>();
		
		images.add(profileImage);
		
		if (profileImage==null) {
			
			throw new ResourceNotFoundException(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE,imageId);
			
		}

		Integer usedUserImage = userRepository.findUserCountByImageId(profileImage.getId());

		if (usedUserImage > 0) {
			throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
		}

		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
		}

		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);

		Set<Role> roles = new HashSet<>();
		roles.add(role);

		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
		User user = new User();
		user.getMyImages().add(profileImage);
		user.setProfileImage(profileImage);
		user.setCreateAt(LocalDateTime.now());
		user.setRoles(roles);
		user.setPassword(encodedPassword);
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setAddress(registerRequest.getAddress());
		user.setPostCode(registerRequest.getPostCode());
		user.setPhone(registerRequest.getPhone());

		userRepository.save(user);

	}
//--------------------search users by name-----------------
	public List<User> searchUserByName(String firstName) {

		return userRepository.searchUsersByUserName(firstName);
	}

	public void deleteUserById(Long id) {

		userRepository.deleteById(id);

	}

//------------- find user by email-------------------
	public User getUserByEmail(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email)));
		return user;
	}

//------------ convert roles------------------
	public Set<Role> convertRoles(Set<String> pRoles) {
		Set<Role> roles = new HashSet<>();

		if (pRoles == null) {
			Role userRole = roleService.findByType(RoleType.ROLE_ANONYMOUS);
			roles.add(userRole);
		} else {
			pRoles.forEach(roleStr -> {
				if (roleStr.equals(RoleType.ROLE_ADMIN.getName())) { // Administrator
					Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
					roles.add(adminRole);

				} else {
					Role userRole = roleService.findByType(RoleType.ROLE_ANONYMOUS);
					roles.add(userRole);
				}
			});
		}

		return roles;
	}


	

}
