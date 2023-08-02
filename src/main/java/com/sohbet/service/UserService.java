package com.sohbet.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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
import com.sohbet.request.AdminUserUpdateRequest;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UpdatePasswordRequest;
import com.sohbet.request.UpdateUserRequest;
import com.sohbet.security.SecurityUtils;
import com.sohbet.security.jwt.JwtUtils;

@Service
public class UserService {

	private  UserRepository userRepository;
	
	private  UserMapper userMapper;
	
	private  ImageService imageService;
	
	private  RoleService roleService;
	
	private PasswordEncoder passwordEncoder;
	
	private  JwtUtils jwtUtils;
	
	@Autowired
	public UserService( UserRepository userRepository,UserMapper userMapper,ImageService imageService,
			            RoleService roleService,PasswordEncoder passwordEncoder,JwtUtils jwtUtils) {
		
		this.userRepository=userRepository;
		this.userMapper=userMapper;
		this.imageService=imageService;
		this.roleService=roleService;
		this.passwordEncoder=passwordEncoder;
		this.jwtUtils=jwtUtils;
		
	
	}
	
	public UserDTO getPrincipal() {
		 User currentUser =  getCurrentUser();
		  UserDTO userDTO = userMapper.userToUserDto(currentUser);
		  return userDTO;
	
		
	}
	
	//---------- get current user ----------------------
	public User getCurrentUser() {
		
		String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
		 new ResourceNotFoundException(String.format(ErrorMessage.PRINCIPAL_FOUND_MESSAGE)));
		User user=  getUserByEmail(email);
		return user ;
	}
	
		
		
//--------------- get user by id ----------------
		public UserDTO getUserById(Long id) {
			
		User user =	userRepository.findById(id).orElseThrow(()-> new  ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE,id)));
			
	UserDTO userDTO =	userMapper.userToUserDto(user);
			return userDTO;
			
			
			
		}
		

		// ------------- find user by email ---------------
			public User getUserByEmail(String email) {
				
		        User	user =	userRepository.findByEmail
				                            (email).orElseThrow(
				                                  ()-> new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_NOT_FOUND)));
			

		return user;
			}
		
		//--------------- get user by password ----------------
		public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
			
			     User user = getCurrentUser();
			     
			     // builtIn mi kontrol ediyoruz
			     if(user.getBuiltIn()) {
			    	 throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
			    	 
			     }
			     // Formda girilen OldPassword bilgisi ile DB deki password aynı mı kontrol ediyoruz
			     if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
			    	  throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
			     }
			     
			     // yeni gelen password encode ediliyor
			     String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
			     user.setPassword(hashedPassword);
			     userRepository.save(user);
			     
			    /* transactional açıklaması için eklendi
			     if(true) {
			    	 throw new BadRequestException("Exception");
			     }
			     */
			   
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
		
	
		
	//------------------ save user ------------------------
		public void saveUser(String imageId,RegisterRequest registerRequest) {
			if(userRepository.existsByEmail(registerRequest.getEmail())) {
				throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
			}
               Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
			
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			
			String encodedPassword =  passwordEncoder.encode(registerRequest.getPassword());
			
			User user = new User();
			
			Image imageFile =getImage(imageId);
			Set<Image> image = new HashSet<>();
	     	image.add(imageFile);
	     	
//			Integer usedUserImageCount= userRepository.findCountingById(imageFile);
//			
//		if (usedUserImageCount > 0) {
//			throw new ResourceNotFoundException(ErrorMessage.IMAGE_USED_MESSAGE);
//		}

			user.setImage(image);		
			user.setFirstName(registerRequest.getFirstName());
			user.setLastName(registerRequest.getLastName());
			user.setEmail(registerRequest.getEmail());
			user.setPassword(encodedPassword);
			user.setUpdateAt(registerRequest.getUpdateAt());
			user.setCreateAt(registerRequest.getCreateAt());
			user.setPhoneNumber(registerRequest.getPhoneNumber());
			user.setAddress(registerRequest.getAddress());
			user.setRole(roles);
			
		
		
			
			userRepository.save(user);
			
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
	
	//------------find user profil-------------------

	public UserDTO findUserProfile(String token) {
		
		String email=jwtUtils.getEmailFromToken(token);
		
		if (email==null) {
			throw new BadRequestException(ErrorMessage.UNVALID_TOKEN);
			
		}
		User user=getUserByEmail(email);
		
		if (user==null) {
			throw new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE);
		}
		
	UserDTO userDTO=	userMapper.userToUserDto(user);
		
		return userDTO;
	}
	
	//------------- search users ----------------------
	
	public List<UserDTO> searchUsers(String query){
		List<User> userDTOs=userRepository.searchUser(query);
		List<UserDTO> userDTOlist=	userMapper.userToUserDTOList(userDTOs);
		
		return userDTOlist;
	}


    //----------------- user update auth----------------------
public void updateUserAuth(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
		
		UserDTO userDto = getUserById(id);
		User user =userMapper.userDTOToUser(userDto);
		
		 // builtIn mi kontrol ediyoruz
	     if(user.getBuiltIn()) {
	    	 throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	    	 
	     }
	    
	      boolean emailExist  = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());
	      
	      if(emailExist && ! adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
	    	  throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, 
	    			  adminUserUpdateRequest.getEmail()));
	      }
	      
	      // password boş ise
	      if(adminUserUpdateRequest.getPassword()==null) {
	    	  adminUserUpdateRequest.setPassword(user.getPassword());
	      } else  {
	    	  String encodedPassword =  passwordEncoder.encode(adminUserUpdateRequest.getPassword());
	    	  adminUserUpdateRequest.setPassword(encodedPassword);
	      }
	      
	      // Customer    ----  ROLE_CUSTOMER
	      // Administrator   ---- ROLE_ADMIN
	       Set<String> userStrRoles =   adminUserUpdateRequest.getRole();
	       
	       Set<Role> roles = convertRoles(userStrRoles);
	       
	       Set<String> userimage =   adminUserUpdateRequest.getImage();
	       
	      Set<Image> image= getImage(userimage);
	       
	      user.setImage(image);
	       user.setFirstName(adminUserUpdateRequest.getFirstName());
	       user.setLastName(adminUserUpdateRequest.getLastName());
	       user.setEmail(adminUserUpdateRequest.getEmail());
	       user.setPassword(adminUserUpdateRequest.getPassword());
	       user.setPhoneNumber(adminUserUpdateRequest.getPhoneNumber());
	       user.setAddress(adminUserUpdateRequest.getAddress());
	       user.setBuiltIn(adminUserUpdateRequest.getBuiltIn() );
	       
	       user.setRole(roles);
	       
	       userRepository.save(user);
	       
			
		}

		private Set<Image> getImage(Set<String> imageUrls) {
				 Set<Image> images = new HashSet<>();
			        for (String imageUrl : imageUrls) {
			            Image image = new Image();
			            image.setId(imageUrl);   // Eğer Image sınıfında başka alanlar varsa, diğer alanları da ayarlayabilirsiniz
			            images.add(image);
			        }
			        return images;
			
		}
		//-------------- Converts Role methodu--------------
		public Set<Role> convertRoles(Set<String> pRoles) {
			Set<Role> roles = new HashSet<>();
			
			if(pRoles==null) {
				 Role userRole =  roleService.findByType(RoleType.ROLE_CUSTOMER);
				 roles.add(userRole);
			}else {
				pRoles.forEach(roleStr->{
					if(roleStr.equals(RoleType.ROLE_ADMIN.getName())) { // Administrator
						 Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
						roles.add(adminRole);
						
					}else {
						Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
						roles.add(userRole);
					}
				});
			}
			
			return roles;
		}
		
}
		

