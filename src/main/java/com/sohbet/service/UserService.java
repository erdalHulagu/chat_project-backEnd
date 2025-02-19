package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.security.config.SecurityUtils;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	// ------------------ get current user ------------------------
	public User getCurrentUser() {

		String email = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		User user = getUserByEmail(email);

		return user;

	}

// -------------------  get userDTO by id --------------

	@Transactional
	public UserDTO getUser(Long id) {

		User user = getUserById(id);

		UserDTO userDTO = userMapper.userToUserDto(user);
		return userDTO;

	}

// -------------------  get user by id --------------

	public User getUserById(Long id) {

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

	// --------------------get all users pageable------------------------
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
	public Set<UserDTO> getAllUsers() {
		List<User> userList = userRepository.findAll();
		Set<User> userSet = userList.stream().collect(Collectors.toSet());

		if (userList.isEmpty()) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_LIST_IS_EMPTY));
		}
		Set<UserDTO> userDTOList = userMapper.mapUserListToUserDTOList(userSet);
		return userDTOList;
	}

	public UserDTO updateUser(UpdateUserRequest updateUserRequest) {
		User user = getCurrentUser();

		if (user == null) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE));
		}

		boolean emailExist = userRepository.existsByEmail(updateUserRequest.getEmail());
		if (emailExist && (!updateUserRequest.getEmail().equals(user.getEmail()))) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, updateUserRequest.getEmail()));
		}

		String imageId = user.getProfileImage().getId();

		List<User> userList = userRepository.findUserByImageId(imageId);

		for (User u : userList) {
			// bana gelen user Id si ile yukardakiList türündeki userId leri eşit olmaları
			// lazım,
			// eğer eşit değilse girilenm image başka bir user için yüklenmiş
			if (user.getId().longValue() != u.getId().longValue()) {
				throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
			}

		}

		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);
		user.getRoles().add(role);
		user.setUpdateAt(LocalDateTime.now());
		user.setFirstName(updateUserRequest.getFirstName());
		user.setLastName(updateUserRequest.getLastName());
		user.setAddress(updateUserRequest.getAddress());
		user.setPhone(updateUserRequest.getPhone());
		user.setEmail(updateUserRequest.getEmail());
		user.setPostCode(updateUserRequest.getPostCode());

		User newuser = userRepository.save(user);
		return userMapper.userToUserDto(newuser);
	}

	public Image upDateUserImages(User user, String imageId) {

		Image image = imageService.getImageById(imageId);

		List<User> userList = userRepository.findUserByImageId(image.getId());

		for (User u : userList) {
			// bana gelen user Id si ile yukardakiList türündeki userId leri eşit olmaları
			// lazım,
			// eğer eşit değilse girilenm image başka bir user için yüklenmiş
			if (user.getId().longValue() != u.getId().longValue()) {
				throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
			}

		}
		user.setProfileImage(image);
		user.getMyImages().add(image);

		User usr = userRepository.save(user);

		return usr.getProfileImage();
	}
//	public UserDTO updateUser(UpdateUserRequest updateUserRequest, String imageId) {
//		User user = getCurrentUser();
//		
//		if (user == null) {
//			throw new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE));
//		}
//		
//		boolean emailExist = userRepository.existsByEmail(updateUserRequest.getEmail());
//		if (emailExist && (!updateUserRequest.getEmail().equals(user.getEmail()))) {
//			throw new ConflictException(
//					String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, updateUserRequest.getEmail()));
//		}
//		
//		
//		Image image = imageService.getImageById(imageId);
//		
//		if (image==null) {
//			
//			image=null;
//		}
//		
//		
//		List<User> userList = userRepository.findUserByImageId(image.getId());
//		
//		for (User u : userList) {
//			// bana gelen user Id si ile yukardakiList türündeki userId leri eşit olmaları
//			// lazım,
//			// eğer eşit değilse girilenm image başka bir user için yüklenmiş
//			if (user.getId().longValue() != u.getId().longValue()) {
//				throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
//			}
//			
//		}
//		
//		
//		user.setProfileImage(image); // Sadece `profileImage` null değilse ayarlanır
//		user.getMyImages().add(image);
//		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);
//		user.getRoles().add(role);
//		user.setUpdateAt(LocalDateTime.now());
//		user.setFirstName(updateUserRequest.getFirstName());
//		user.setLastName(updateUserRequest.getLastName());
//		user.setAddress(updateUserRequest.getAddress());
//		user.setPhone(updateUserRequest.getPhone());
//		user.setEmail(updateUserRequest.getEmail());
//		user.setPostCode(updateUserRequest.getPostCode());
//		
//		User newuser =userRepository.save(user);
//		return userMapper.userToUserDto(newuser);
//	}

	// ---------------- register user----------------------

	public void saveUser(RegisterRequest registerRequest) {

		Image profileImage = imageService.getImageById(registerRequest.getProfileImage());

		Set<Image> imFiles = new HashSet<>();
		imFiles.add(profileImage);

		// set user role
		Role role = roleService.findByType(RoleType.ROLE_ANONYMOUS);

		// encode password
		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

		// check email if exist
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
		}

		User user = new User();
		user.setMyImages(imFiles);
		user.setProfileImage(profileImage);

//		 Integer usedUserImage =
//		 userRepository.findUserCountByImageId(profileImage.getId());
//		
//		 if (usedUserImage > 0) {
//		 throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
//		 }

		user.setCreateAt(LocalDateTime.now());
		user.getRoles().add(role);
		user.setPassword(encodedPassword);
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setAddress(registerRequest.getAddress());
		user.setPostCode(registerRequest.getPostCode());
		user.setPhone(registerRequest.getPhone());

		userRepository.save(user);

	}

	// --------------------search users by imageId-----------------
	public UserDTO findUserByImageId(String imageId) {

		Image image = imageService.getImageById(imageId);

		if (image == null) {

			throw new ResourceNotFoundException(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE);

		}

		List<User> users = userRepository.findAll();
		User user = new User();
		for (User u : users) {
			if (u.getProfileImage().getId().equals(image.getId())) {
				user = u;
			} else {
				throw new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, imageId));
			}

		}
		return userMapper.userToUserDto(user);
	}

	// --------------------search users by name-----------------
	public List<User> searchUserByName(String firstName) {

		return userRepository.searchUsersByUserName(firstName);
	}

	public void deleteUserById(Long id) {

		userRepository.deleteById(id);

	}

//------------------ add User As Friend ------------------
	public void addUserAsFriend(User currentUser, Long friendId) {
		User userToAdd = getUserById(friendId);
		boolean isFriend = currentUser.getFriends().stream().anyMatch(fr -> fr.getId().equals(userToAdd.getId()));
		if (isFriend) {
			throw new ConflictException(String.format(ErrorMessage.THIS_USER_ALREADY_EXIST));
		}

		currentUser.getFriends().add(userToAdd);
		userToAdd.getFriends().add(currentUser);

		userRepository.save(currentUser);
		userRepository.save(userToAdd);

	}

	// ------------------ add image to your images ------------------
	public void addImageToYourImages(User currentUser, String imageId) {
		Image image = imageService.getImageById(imageId);
		Set<Image> imgSet = new HashSet<>();
		imgSet.add(image);
		currentUser.setMyImages(imgSet);
		userRepository.save(currentUser);

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
	
	
	public void deleteUserImageById(String imageId) {
		
	    User user = userMapper.userDTOToUser(findUserProfile()); 

	    if (user.getMyImages().removeIf(img -> img.getId().equalsIgnoreCase(imageId))) {
	        imageService.removeById(imageId); 
	        userRepository.save(user); 
	        
	    } else {
	        throw new BadRequestException(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE);
	        
	    }
	}
	

//	public Set<Image> findImagesByUserId(Long userId) {
//		 return userRepository.findUserImages(userId);
//		
//	}

}
