package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.ImageData;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.exception.ConflictException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.ImageRepository;
import com.sohbet.repository.UserRepository;
import com.sohbet.request.RegisterRequest;
import com.sohbet.security.config.SecurityUtils;

@Service
public class UserService {

	private UserRepository userRepository;

	private ImageService imageService;

	private UserMapper userMapper;

	private RoleService roleService;

	private PasswordEncoder passwordEncoder;
	
	private ImageRepository imageRepository;

	public UserService(UserRepository userRepository, @Lazy ImageService imageService, UserMapper userMapper,
			RoleService roleService, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.imageService = imageService;
		this.userMapper = userMapper;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;

	}

	// ------------------ get current user ------------------------
	public User getCurrentUser() {

		String email = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		User user = getUserByEmail(email);
		return user;

	}

	// ------------------ get current userDTO ------------------------
	public UserDTO getPrincipal() {
		User currentUser = getCurrentUser();
		UserDTO userDTO = userMapper.userToUserDto(currentUser);
		return userDTO;

	}

// -------------------  get user by id --------------
	public UserDTO getUserById(Long id) {

		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, id)));

		UserDTO userDTO = userMapper.userToUserDto(user);
		return userDTO;

	}
// -------------------  get user by id --------------
	public User getUser(Long id) {
		
		User user = userRepository.findUserById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, id)));
		
	Long imgId =userRepository.getImage(user.getProfileImage().getId());
//		UserDTO userDTO = userMapper.userToUserDto(user);
//		return userDTO;
Optional<Image> strId=imageRepository.findById(imgId.toString());
	user.getProfileImage().setId(strId.toString());
		return user;
	}

//
//	public Page<User> getAll(Pageable pageable) {
//
//		Page<User> userPage = userRepository.findAll(pageable);
//		
//		
//		return userPage;
//	}
	// --------------------get user profile------------------------
	public UserDTO findUserProfile() {

		User user = getCurrentUser();

		if (user == null) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, user));

		}
		UserDTO userDTO = userMapper.userToUserDto(user);
		return userDTO;

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
		List<UserDTO> userDTOList = userMapper.userToUserDTOList(userList);
		return userDTOList;
	}

	// ------------------update user---------------------
	public void updateUser(User user, String imageId, UserDTO userDTO) {

		if ((user == null)) {
			new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_IS_NOT_MATCH));

		}
		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);

		Image image = imageService.getImageById(imageId);
		Set<Image>images=new HashSet<>();
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
	
		user.getMyImages().add(image);
		user.setUpdateAt(LocalDateTime.now());
		User usr = userMapper.userDTOToUser(userDTO);

		userRepository.save(usr);
	}

	// ---------------- register user----------------------

	public void saveUser(String id, RegisterRequest registerRequest) {

		Image profileImage = imageService.getImageById(id);
		
		Set<Image>images=new HashSet<>();
		
		images.add(profileImage);
		
		if (profileImage==null) {
			
			throw new ResourceNotFoundException(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE,id);
			
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
		user.setRoles(roles);
		user.setPassword(encodedPassword);
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setAddress(registerRequest.getAddress());
		user.setPhone(registerRequest.getPhone());

		userRepository.save(user);

	}
	

	public void deleteUserWithId(Long id) {

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
			Role userRole = roleService.findByType(RoleType.ROLE_ADMIN);
			roles.add(userRole);
		} else {
			pRoles.forEach(roleStr -> {
				if (roleStr.equals(RoleType.ROLE_ADMIN.getName())) { // Administrator
					Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
					roles.add(adminRole);

				} else {
					Role userRole = roleService.findByType(RoleType.ROLE_ADMIN);
					roles.add(userRole);
				}
			});
		}

		return roles;
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

	// ------------ get image by string id ------------------ extra
//public Image getImage (String id) {
//	Image imageFile =imageService.findImageByImageId(id);
//	return imageFile;
//}
//	

}
