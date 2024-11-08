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
import com.sohbet.enums.RoleType;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "stringToImage")
	User userDTOToUser(UserDTO userDTO);


	@Mapping(target = "profileImage", ignore = true)
	UserDTO userToUserDto(User user);
	
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "stringToImage")
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "chatAdmins", ignore = true)
	User registerUserToUser(RegisterRequest registerRequest);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "profileImage", ignore = true)
	User userRequestToUser(UserRequest userRequest);

	
	List<UserDTO> userToUserDTOList(List<User> userList);
	
	
	Set<UserDTO> userToUserDTOSetList(Set<User> setUsers);

//	@Named("getRoleAsString")
//	public static Set<String> mapRoles(Set<Role> roles) {
//
//		Set<String> roleStr = new HashSet<>();
//
//		roles.forEach(r -> {
//			roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek
//
//		});
//
//		return roleStr;
//	}

	

	@Named("getImageCollectionAsImage")
	public static Set<Image> mapping(Set<String> imageUrls) {
		Set<Image> images = new HashSet<>();
		for (String imageUrl : imageUrls) {
			Image image = new Image();
			image.setId(imageUrl);
			images.add(image);
		}
		return images;
	}

	@Named("getImageCollectionAsString")
	public static Set<String> getImageIds(Set<Image> imageFiles) {
		Set<String> imgs = new HashSet<>();
		imgs = imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
		return imgs;
	}

	@Named("imageToString")
    default String imageToString(Image image) {
        return image != null ? image.getId() : null;
    }

    @Named("stringToImage")
    default Image stringToImage(String id) {
        if (id == null) {
            return null;
        }
        Image image = new Image();
        image.setId(id);
        return image;
    }


}
