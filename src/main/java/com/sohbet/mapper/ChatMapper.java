package com.sohbet.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "getImageAsString")
    @Mapping(target = "users", source = "users", qualifiedByName = "mapUserToLong")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessageToLong")
    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUserToLong")
    @Mapping(target = "createdBy", source = "createdBy.id")
    ChatDTO chatToChatDTO(Chat chat);

    List<ChatDTO> mapChatListToChatDTOList(List<Chat> chatList);

    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "getImageStringToImage")
    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapLongToUser")
    @Mapping(target = "users", source = "users", qualifiedByName = "mapLongToUser")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapLongToMessage")
    @Mapping(target = "createdBy.id", source = "createdBy")
    Chat chatDTOToChat(ChatDTO chatDTO);

    //---------user map ---------
    @Named("mapLongToUser")
    public static Set<User> mapLongToUser(Set<Long> userIds) {
        Set<User> users = new HashSet<>();
        for (Long userId : userIds) {
            User user = new User();
            user.setId(userId);
            users.add(user);
        }
        return users;
    }

    @Named("mapUserToLong")
    public static Set<Long> mapUserToLong(Set<User> users) {
        return users != null ? users.stream().map(User::getId).collect(Collectors.toSet()) : new HashSet<>();
    }
    
    //---------message map ---------
    @Named("mapLongToMessage")
    public static List<Message> mapLongToMessage(List<Long> messagesIds) {
        List<Message> messages = new ArrayList<>();
        for (Long messageId : messagesIds) {
            Message message = new Message();
            message.setId(messageId);
            messages.add(message);
        }
        return messages;
    }

    @Named("mapMessageToLong")
    public static List<Long> mapMessageToLong(List<Message> messages) {
        return messages != null ? messages.stream().map(Message::getId).collect(Collectors.toList()) : new ArrayList<>();
    }
    
    //----------image map-------------
    @Named("getImageAsString")
    public static Set<String> getImageAsString(Set<Image> imageFiles) {
        return imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
    }

    @Named("getImageStringToImage")
    public static Set<Image> getImageStringToImage(Set<String> imageUrls) {
        Set<Image> images = new HashSet<>();
        for (String imageUrl : imageUrls) {
            Image image = new Image();
            // Eğer Image id Long türündeyse:
            image.setId(imageUrl);
            images.add(image);
        }
        return images;
    }
}