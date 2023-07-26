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
import com.sohbet.request.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	
	@Mapping(target = "id", ignore=true)
	@Mapping(target = "image" , ignore = true )
	@Mapping(target = "role", ignore=true)
	User userDTOToUser(UserDTO userDTO);
  
	
	@Mapping(target = "id", ignore=true)
	@Mapping(target = "builtIn", ignore=true)
	@Mapping(target = "password", ignore=true)
	@Mapping(target = "role", ignore=true)
	@Mapping(target = "image",source = "image",qualifiedByName = "getImageAsLong")
	User userRequestToUser(UserRequest userRequest);
	


	@Mapping(target = "image",source = "image",qualifiedByName = "getImageAsString")
	UserDTO userToUserDto(User user);
	
	
	List<UserDTO> userToUserDTOList(List<User> userList);
	  
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
//	 @Named("getRoleAsInteger")
//	   public static Set<Role> mapRole(Set<Integer> roleUrls) {
//			 Set<Role> roles = new HashSet<>();
//		        for (Integer roleUrl : roleUrls) {
//		            Role role = new Role();
//		            role.setId(roleUrl);   // Eğer Image sınıfında başka alanlar varsa, diğer alanları da ayarlayabilirsiniz
//		            roles.add(role);
//		        }
//		        return roles;
//}
	 
	 
}

