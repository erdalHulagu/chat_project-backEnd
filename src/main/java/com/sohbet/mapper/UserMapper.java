package com.sohbet.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Role;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // UserDTO -> User dönüşümü
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "myImages", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "chatList", ignore = true)
    @Mapping(target = "chats", ignore = true)
    @Mapping(target = "chatAdmins", ignore = true)
    User userDTOToUser(UserDTO userDTO);

    // User -> UserDTO dönüşümü

    @Mapping(source = "myImages", target = "myImages", qualifiedByName = "mapImages")
    @Mapping(source = "profileImage.id", target = "profileImage")
    @Mapping(source = "chatList", target = "chatList", qualifiedByName = "mapChats")
    @Mapping(target = "chats", ignore = true)
    @Mapping(source = "chatAdmins", target = "chatAdmins", qualifiedByName = "mapChats")
    UserDTO userToUserDto(User user);

    List<UserDTO> mapUserListToUserDTOList(List<User> chatList);
    Set<UserDTO> mapUserSetListToUserDTOList(Set<User> chatList);
 // Role -> String dönüşümü
    @Named("mapRolesToSetString")
    default Set<String> mapRolesToSetString(Set<Role> roles) {
        return roles.stream().map(role -> role.getType().getName()).collect(Collectors.toSet());
    }

    @Named("mapRolesToSetRole")
    default Set<Role> mapRolesToSetRole(Set<String> roles) {
        return roles.stream().map(roleName -> {
            Role role = new Role();
            // Convert String role name to RoleType
            role.setType(RoleType.valueOf(roleName)); // Ensure roleName matches enum values
            return role;
        }).collect(Collectors.toSet());
    }


    // Image -> String dönüşümü
    @Named("mapImages")
    default Set<String> mapImages(Set<Image> images) {
        return images.stream().map(Image::getId).collect(Collectors.toSet());
    }

    // Chat -> ChatDTO dönüşümü için yardımcı metot
    @Named("mapChats")
    default List<ChatDTO> mapChats(List<Chat> chats) {
        return chats.stream().map(ChatMapper.INSTANCE::chatToChatDTO).collect(Collectors.toList());
    }

    @Named("mapChats")
    default Set<ChatDTO> mapChats(Set<Chat> chats) {
        return chats.stream().map(ChatMapper.INSTANCE::chatToChatDTO).collect(Collectors.toSet());
    }
}
