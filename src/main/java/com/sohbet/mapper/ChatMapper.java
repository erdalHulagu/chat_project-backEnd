package com.sohbet.mapper;

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
import com.sohbet.domain.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    // Chat -> ChatDTO dönüşümü
    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUsersToUserDTOs")
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "messages", ignore = true) // Mesajlar için ayrı bir dönüşüm yapılabilir
    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapImagesToStrings")
    ChatDTO chatToChatDTO(Chat chat);

    // ChatDTO -> Chat dönüşümü
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapLongToUser")
    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUserDTOsToUsers")
    @Mapping(target = "users", source = "users", qualifiedByName = "mapUserDTOsToUsers")
    @Mapping(target = "messages", ignore = true) // Mesajlar için ayrı bir dönüşüm yapılabilir
    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapStringsToImages")
    Chat chatDTOToChat(ChatDTO chatDTO);

    List<ChatDTO> chatListToChatDTOList(List<Chat> chats);

    // User -> UserDTO dönüşümü
    @Named("mapUsersToUserDTOs")
    public static Set<UserDTO> mapUsersToUserDTOs(Set<User> users) {
        return users != null ? users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toSet()) : new HashSet<>();
    }

    @Named("mapUserDTOsToUsers")
    public static Set<User> mapUserDTOsToUsers(Set<UserDTO> userDTOs) {
        return userDTOs != null ? userDTOs.stream().map(UserMapper.INSTANCE::userDTOToUser).collect(Collectors.toSet()) : new HashSet<>();
    }

    @Named("mapLongToUser")
    public static User mapLongToUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    // Image -> String dönüşümü
    @Named("mapImagesToStrings")
    public static Set<String> mapImagesToStrings(Set<Image> images) {
        return images.stream().map(Image::getId).collect(Collectors.toSet());
    }

    // String -> Image dönüşümü
    @Named("mapStringsToImages")
    public static Set<Image> mapStringsToImages(Set<String> imageUrls) {
        Set<Image> images = new HashSet<>();
        for (String imageUrl : imageUrls) {
            Image image = new Image();
            image.setId(imageUrl);
            images.add(image);
        }
        return images;
    }
}
