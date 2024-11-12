package com.sohbet.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "stringToImage")
	@Mapping(target = "chatList", source = "chatList", qualifiedByName = "mapLonListToChat")
	@Mapping(target = "chats", source = "chats", qualifiedByName = "mapLonListToChat")
	@Mapping(target = "chatAdmins", source = "chatAdmins", qualifiedByName = "mapLongToChat")
	User userDTOToUser(UserDTO userDTO);

	@Mapping(target = "chatList", source = "chatList", qualifiedByName = "mapChatListToLong")
	@Mapping(target = "chats", source = "chats", qualifiedByName = "mapChatListToLong")
	@Mapping(target = "chatAdmins", source = "chatAdmins", qualifiedByName = "mapChatSetToLong")
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsString")@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "imageToString")
	UserDTO userToUserDto(User user);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "stringToImage")
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "roles", source = "roles", ignore = true)
	User registerUserToUser(RegisterRequest registerRequest);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "stringToImage")
	User userRequestToUser(UserRequest userRequest);

	List<UserDTO> mapUserListToUserDTOList(List<User> chatList);
	
//---------- Role map ----------
	Set<UserDTO> userToUserDTOSetList(Set<User> setUsers);
	@Named("mapRolesToString")
	public static Set<String> mapRolesToString(Set<Role> roles) {
		return roles != null ? roles.stream().map(role -> role.getType().getName()).collect(Collectors.toSet())
				: new HashSet<>();
	}

	//---------- chat map ----------
	@Named("mapChatSetToLong")
	public static Set<Long> mapChatSetToLong(Set<Chat> chats) {
		return chats != null ? chats.stream().map(Chat::getId).collect(Collectors.toSet()) : new HashSet<>();
	}
	
	@Named("mapChatListToLong")
	public static List<Long> mapChatListToLong(List<Chat> chats) {
		return chats != null ? chats.stream().map(Chat::getId).collect(Collectors.toList()) : new ArrayList<>();
	}

	//---------- chat map ----------
	@Named("mapLongToChat")
	public static Set<Chat> mapLongToChat(Set<Long> ids) {
		Set<Chat> chats = new HashSet<>();
		ids.forEach(id -> {
			Chat chat = new Chat();
			chat.setId(id); // Assumes only setting ID is safe
			chats.add(chat);
		});
		return chats;
	}
	//---------- chat map ----------
	@Named("mapLonListToChat")
	public static List<Chat> mapLongToChat(List<Long> ids) {
		List<Chat> chats = new ArrayList<>();
		ids.forEach(id -> {
			Chat chat = new Chat();
			chat.setId(id); // Assumes only setting ID is safe
			chats.add(chat);
		});
		return chats;
	}
	//---------- image map ----------
	@Named("getImageCollectionAsImage")
	public static Set<Image> mapping(Set<String> imageUrls) {
		Set<Image> images = new HashSet<>();
		imageUrls.forEach(imageUrl -> {
			Image image = new Image();
			image.setId(imageUrl); // Adjust here if ID is Long
			images.add(image);
		});
		return images;
	}
	//---------- image map ----------
	@Named("getImageCollectionAsString")
	public static Set<String> getImageIds(Set<Image> imageFiles) {
		Set<String> imgs = new HashSet<>();
		imgs = imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
		return imgs;
	}

	//---------- image map ----------
	@Named("imageToString")
	default String imageToString(Image image) {
		return image != null ? image.getId() : null;
	}

	//---------- image map ----------
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
