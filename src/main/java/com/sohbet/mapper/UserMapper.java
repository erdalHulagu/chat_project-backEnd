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
import com.sohbet.DTO.MessageDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Message;
import com.sohbet.domain.Role;

@Mapper(componentModel = "spring", uses = { ChatMapper.class })
public interface UserMapper {

	UserMapper USERMAPPER = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "profileImage")
	@Mapping(source = "myImages", target = "myImages", qualifiedByName = "mapImages")
	@Mapping(source = "chatList", target = "chatList", qualifiedByName = "mapChatsToChatDTOList")
	@Mapping(source = "chats", target = "chats", qualifiedByName = "mapChatsToChatDTOList")
	@Mapping(source = "messages", target = "messages", qualifiedByName = "mapMessagesToMessageDTOs")
	UserDTO userToUserDto(User user);

	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "chatList", ignore = true)
	@Mapping(target = "messages", ignore = true)
	@Mapping(target = "myImages", ignore = true)
	@Mapping(target = "profileImage.id", source = "profileImage")
	User userDTOToUser(UserDTO userDTO);

	Set<UserDTO> mapUserListToUserDTOList(Set<User> userList);

	@Named("mapRolesToSetString")
	default Set<String> mapRolesToSetString(Set<Role> roles) {
		return roles != null ? roles.stream().map(role -> role.getType().getName()).collect(Collectors.toSet())
				: new HashSet<>();
	}

	@Named("mapRolesToSetRole")
	default Set<Role> mapRolesToSetRole(Set<String> roleNames) {
		return roleNames != null ? roleNames.stream().map(roleName -> {
			Role role = new Role();
			role.setType(RoleType.valueOf(roleName));
			return role;
		}).collect(Collectors.toSet()) : new HashSet<>();
	}

	@Named("mapImages")
	default Set<String> mapImages(Set<Image> images) {
		return images != null ? images.stream().map(img -> img.getId()).collect(Collectors.toSet()) : new HashSet<>();
	}

	@Named("profileImage")
	default String profileImage(Image image) {
		return image != null ? image.getId() : null;
	}

	@Named("mapChatsToChatDTOList")
	default List<ChatDTO> mapChatsToChatDTOList(List<Chat> chats) {
		return chats != null ? chats.stream().map(ChatMapper.CHATMAPPER::chatToChatDTO).collect(Collectors.toList())
				: new ArrayList<>();
	}

	@Named("mapChatsToChatDTOSet")
	default Set<ChatDTO> mapChatsToChatDTOSet(Set<Chat> chats) {
		return chats != null ? chats.stream().map(ChatMapper.CHATMAPPER::chatToChatDTO).collect(Collectors.toSet())
				: new HashSet<>();
	}
}
