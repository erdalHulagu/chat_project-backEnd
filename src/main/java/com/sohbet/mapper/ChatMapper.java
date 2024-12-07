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
import com.sohbet.DTO.MessageDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;

@Mapper(componentModel = "spring")
public interface ChatMapper {

	ChatMapper CHATMAPPER = Mappers.getMapper(ChatMapper.class);

//	@Mapping(target = "createdBy", source = "createdBy.id")
//	@Mapping(target = "admins", ignore = true)
//	@Mapping(target = "users", ignore = true)
////    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUsersToUserDTOs")
////    @Mapping(target = "users", source = "users", qualifiedByName = "mapUsersToUserDTOs")
//	@Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessagesToMessageDTOs") // Avoid potential loops
//	@Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapImageIdToString")
//	@Mapping(target = "chatProfileImage", source = "chatProfileImage.id")
//	ChatDTO chatToChatDTO(Chat chat);
//
//	@Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapLongToUser")
//	@Mapping(target = "admins", source = "admins", qualifiedByName = "mapUserDTOsToUsers")
//	@Mapping(target = "users", source = "users", qualifiedByName = "mapUserDTOsToUsers")
//	@Mapping(target = "chatProfileImage.id", source = "chatProfileImage")
//	@Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessageDTOsToMessages")
//	@Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapStringToImage")
//	Chat chatDTOToChat(ChatDTO chatDTO);
	@Mapping(target = "createdBy", source = "createdBy.id")
	@Mapping(target = "admins", ignore = true)
	@Mapping(target = "users", ignore = true)
//    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUsersToUserDTOs")
//    @Mapping(target = "users", source = "users", qualifiedByName = "mapUsersToUserDTOs")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessagesToMessageDTOs")
    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapImageIdToString")
    @Mapping(target = "chatProfileImage", source = "chatProfileImage.id")
    ChatDTO chatToChatDTO(Chat chat);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapLongToUser")
    @Mapping(target = "admins", source = "admins", qualifiedByName = "mapUserDTOsToUsers")
    @Mapping(target = "users", source = "users", qualifiedByName = "mapUserDTOsToUsers")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessageDTOsToMessages")
    @Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "mapStringToImage")
    @Mapping(target = "chatProfileImage", ignore = true)
    Chat chatDTOToChat(ChatDTO chatDTO);


	List<ChatDTO> chatListToChatDTOList(List<Chat> chats);

	
	
	// Mapping helpers
	@Named("mapUsersToUserDTOs")
	static Set<UserDTO> mapUsersToUserDTOs(Set<User> users) {
		return users != null ? users.stream().map(UserMapper.USERMAPPER::userToUserDto).collect(Collectors.toSet())
				: new HashSet<>();
	}

	@Named("mapUserDTOsToUsers")
	static Set<User> mapUserDTOsToUsers(Set<UserDTO> userDTOs) {
		return userDTOs != null
				? userDTOs.stream().map(UserMapper.USERMAPPER::userDTOToUser).collect(Collectors.toSet())
				: new HashSet<>();
	}

	@Named("mapLongToUser")
	static User mapLongToUser(Long userId) {
		if (userId == null)
			return null;
		User user = new User();
		user.setId(userId);
		return user;
	}

	@Named("mapImageIdToString")
	static String mapImageIdToString(Image image) {
		return image != null ? image.getId() : null;
	}

	@Named("mapStringToImage")
	static Image mapStringToImage(String imageId) {
		if (imageId == null)
			return null;
		Image image = new Image();
		image.setId(imageId);
		return image;
	}
	@Named("mapMessagesToMessageDTOs")
    static List<MessageDTO> mapMessagesToMessageDTOs(List<Message> messages) {
        return messages != null
                ? messages.stream().map(MessageMapper.MESSAGEMAPPER::messageToMessageDTO).toList()
                : null;
    }

    @Named("mapMessageDTOsToMessages")
    static List<Message> mapMessageDTOsToMessages(List<MessageDTO> messageDTOs) {
        return messageDTOs != null
                ? messageDTOs.stream().map(MessageMapper.MESSAGEMAPPER::messageDTOToMessage).toList()
                : null;
    }
//    @Named("mapImageToString")
//    static String mapStringToImage(Image image) {
//    	if (image == null) return null;
//    	
//    	return image.getId();
//    }
}
