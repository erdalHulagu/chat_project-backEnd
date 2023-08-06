package com.sohbet.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visionrent.domain.Role;
import com.visionrent.domain.User;
import com.visionrent.domain.enums.RoleType;
import com.visionrent.dto.UserDTO;
import com.visionrent.dto.request.AdminUserUpdateRequest;
import com.visionrent.dto.request.RegisterRequest;
import com.visionrent.dto.request.UpdatePasswordRequest;
import com.visionrent.dto.request.UserUpdateRequest;
import com.visionrent.exception.BadRequestException;
import com.visionrent.exception.ConflictException;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.mapper.UserMapper;
import com.visionrent.repository.UserRepository;
import com.visionrent.security.SecurityUtils;


@Service
public class UserService {
	
	
	private UserRepository userRepository;
	
	
	private RoleService roleService ;
	

	private PasswordEncoder passwordEncoder;
	
	private UserMapper userMapper;
	
	private ReservationService reservationService;
	
	
	
	@Autowired
	public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, 
			UserMapper userMapper,ReservationService reservationService) {
		super();
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
		this.reservationService = reservationService;
	}


	public User getUserByEmail(String email ) {
		
		  User user  =  userRepository.findByEmail(email).orElseThrow(()->
		  			new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email))
				);
		return user ;
	}


	public void saveUser(RegisterRequest registerRequest) {
		if(userRepository.existsByEmail(registerRequest.getEmail())) {
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
		user.setZipCode(registerRequest.getZipCode());
		user.setRoles(roles);
		
		userRepository.save(user);
		
		
		
	}


	public List<UserDTO> getAllUsers() {
		
		    List<User> users = userRepository.findAll();
		     List<UserDTO> userDTOs = userMapper.map(users);
		     
		     return userDTOs;
		
	}


	public UserDTO getPrincipal() {
		 User currentUser =  getCurrentUser();
		  // return userMapper.userToUserDTO(currentUser);
		  UserDTO userDTO = userMapper.userToUserDTO(currentUser);
		  return userDTO;
	
		
	}
	
	public User getCurrentUser() {
		
		String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
		 new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
		User user =  getUserByEmail(email);
		return user ;
		
	}
	
	/*  BURA ANLATILACAK
	 * 
	public Page<UserDTO> getUserPage(Pageable pageable) {
		Page<User> userPage = userRepository.findAll(pageable);
		Page<UserDTO> userPageDTO = userPage.map(userMapper::userToUserDTO);
		return userPageDTO;
	}
	*/

	
	public Page<UserDTO> getUserPage(Pageable pageable) {
		   Page<User> userPage = userRepository.findAll(pageable);
		   
	        return getUserDTOPage(userPage);
	
	}
	


	private Page<UserDTO> getUserDTOPage(Page<User> userPage) {
		
		 Page<UserDTO> userDTOPage =  userPage.map(new Function<User, UserDTO>() {
			 @Override
			public UserDTO apply(User user) {
				
				return userMapper.userToUserDTO(user);
			}
		});
		 
		 return userDTOPage;
		
	}


	public UserDTO getUserById(Long id) {
		    User user = userRepository.findById(id).orElseThrow(()->
		   
				   new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		    return userMapper.userToUserDTO(user);
	}


	
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		
		     User user = getCurrentUser();
		     
		     // builtIn mi kontrol ediyoruz
		     if(user.getBuiltIn()) {
		    	 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		    	 
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


	@Transactional  
	// // Veritabanı üzerinde gerçekleştirilen bir grup SQL işleminin tek bir bütün 
	//olarak ele alınmasını sağlar, bir veya daha fazla SQL işluseremi tek bir işlem gibi ele alınır.
	public void updateUser( UserUpdateRequest userUpdateRequest) {
		
		User user = getCurrentUser();
		
		  // builtIn mi kontrol ediyoruz
	     if(user.getBuiltIn()) {
	    	 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
	    	 
	     }
	    
	      boolean emailExist  = userRepository.existsByEmail(userUpdateRequest.getEmail());
	      
	      if(emailExist && ! userUpdateRequest.getEmail().equals(user.getEmail())) {
	    	  throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, 
	    			  																													userUpdateRequest.getEmail()));
	      }
	      
	      userRepository.update(user.getId(), userUpdateRequest.getFirstName(), 
	    		  																		userUpdateRequest.getLastName(), 
	    		  																		userUpdateRequest.getPhoneNumber(), 
	    		  																		userUpdateRequest.getEmail(), 
	    		  																		userUpdateRequest.getAddress(), 
	    		  																		userUpdateRequest.getZipCode());
	      
	      

	}


	public void updateUserAuth(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
		
		User user = getById(id);
		
		 // builtIn mi kontrol ediyoruz
	     if(user.getBuiltIn()) {
	    	 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
	    	 
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
	       Set<String> userStrRoles =   adminUserUpdateRequest.getRoles();
	       
	       Set<Role> roles = convertRoles(userStrRoles);
	       
	       user.setFirstName(adminUserUpdateRequest.getFirstName());
	       user.setLastName(adminUserUpdateRequest.getLastName());
	       user.setEmail(adminUserUpdateRequest.getEmail());
	       user.setPassword(adminUserUpdateRequest.getPassword());
	       user.setPhoneNumber(adminUserUpdateRequest.getPhoneNumber());
	       user.setAddress(adminUserUpdateRequest.getAddress());
	       user.setZipCode(adminUserUpdateRequest.getZipCode());
	       user.setBuiltIn(adminUserUpdateRequest.getBuiltIn() );
	       
	       user.setRoles(roles);
	       
	       userRepository.save(user);
	       
		
		
		
		
		
	}
	
	public User getById(Long id) {
		User user =  userRepository.findUserById(id).orElseThrow(()-> new 
				ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
		return user;
	}
	
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

// ********************** DELETE ***************************************
	
	public void removeUserById(Long id) {
		User user = getById(id);
		
		// builtIn mi kontrol ediyoruz
	     if(user.getBuiltIn()) {
	    	 throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
	    	 
	     }
	     
 boolean exist =  reservationService.existsByUser(user);
		 
		 // reservasyon kontrolü
		 if(exist) {
			 throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
		 }
		
	     
	     
	     userRepository.deleteById(id);
	    
		
	}


	public List<User> getUsers() {
		return userRepository.findAll() ;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
