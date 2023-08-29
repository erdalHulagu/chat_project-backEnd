package com.sohbet.mapper;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;


@Mapper(componentModel = "spring")
public interface UserMapper  {
	
	
	@Mapping(target = "id", ignore=true)
	@Mapping(target = "image" , ignore = true )
	@Mapping(target = "roles",ignore = true)
	User userDTOToUser(UserDTO userDTO);
	
	
	@Mapping(target = "id", ignore=true)
	@Mapping(target = "image" , ignore = true )
	@Mapping(target = "roles",ignore = true)
	User registerUserToUser(RegisterRequest registerRequest);
  
	
	@Mapping(target = "id", ignore=true)
//	@Mapping(target = "image",source = "image",qualifiedByName = "getImageAsLong")
	@Mapping(target = "image" , ignore = true )
	@Mapping(target = "roles",ignore = true)
//	@Mapping(target = "password", ignore=true)
	User userRequestToUser(UserRequest userRequest);
	


	@Mapping(target = "image",source = "image",qualifiedByName = "getImageAsString")
	@Mapping(target = "roles",ignore = true)
	UserDTO userToUserDto(User user);
	
	
	List<UserDTO> userToUserDTOList(List<User> userList);
	
	@Named("getRoleAsString")
	public static Set<String> mapRoles(Set<Role> roles) {
		
		Set<String> roleStr = new HashSet<>();
		
       roles.forEach( r-> {
			roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek
			
		}); 
		
		return roleStr;
	}
	
	  
	 // long turunde image i image turunde image e cevidik
	 @Named("getImageAsLong")
   public static Set<Image> map(Set<String> imageUrls) {
		 Set<Image> images = new HashSet<>();
	        for (String imageUrl : imageUrls) {
	            Image image = new Image();
	            image.setId(imageUrl);   // Eğer Image sınıfında başka alanlar varsa, diğer alanları da ayarlayabilirsiniz
	            images.add(image);
	        }
	        return images;
	 }
	 @Named("getImageAsString")
		public static  Set<String> getImageIds( Set<Image> imageFiles) {
			Set<String> imgs = new HashSet<>();
			imgs = imageFiles.stream().map(imFile->imFile.getId().
																	toString()).
																	collect(Collectors.toSet());
			 return imgs;
		}
//
//	 @Named("getImageAsStringForRequset")
//	 public static  Set<Image> getImage( Set<String> imageFiles) {
//			Set<Image> imgs = new HashSet<>();
//			imgs = imageFiles.stream().map(imFile->imFile.
//			 return imgs;
//		}
//	
}
	
