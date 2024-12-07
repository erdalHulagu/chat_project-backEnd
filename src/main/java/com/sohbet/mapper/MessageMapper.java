package com.sohbet.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.sohbet.DTO.MessageDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;




@Mapper(componentModel = "spring")
public interface MessageMapper {
MessageMapper MESSAGEMAPPER=Mappers.getMapper(MessageMapper.class);
	
////    @Mapping(target = "id", ignore = true) // Yeni mesaj oluşturulurken ID yok sayılır
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "chat", ignore = true)
//    Message messageDTOToMessage(MessageDTO messageDTO);
//
//    @Mapping(target = "userId", source = "user",qualifiedByName = "getUserIdAsLong")
//    @Mapping(target = "chatId", source = "chat",qualifiedByName = "getChatIdAsLong")
//    MessageDTO messageToMessageDTO(Message message);
@Mapping(target = "user", source = "userId", qualifiedByName = "mapLongToUser")
@Mapping(target = "chat", source = "chatId", qualifiedByName = "mapLongToChat")
Message messageDTOToMessage(MessageDTO messageDTO);

@Mapping(target = "userId", source = "user", qualifiedByName = "getUserIdAsLong")
@Mapping(target = "chatId", source = "chat", qualifiedByName = "getChatIdAsLong")
MessageDTO messageToMessageDTO(Message message);

    List<MessageDTO> messageToMessageDTOList(List<Message> messages);
    
    @Named("mapLongToUser")
	static User mapLongToUser(Long userId) {
		if (userId == null)
			return null;
		User user = new User();
		user.setId(userId);
		return user;
	}
    @Named("mapLongToChat")
    static Chat mapLongToChat(Long chatId) {
    	if (chatId == null)
    		return null;
    	Chat chat = new Chat();
    	chat.setId(chatId);
    	return chat;
    }

    @Named("getChatIdAsLong")
    static Long getChatIdAsString(Chat chat) {
        return chat != null && chat.getId() != null ? chat.getId() : null;
    }
    @Named("getUserIdAsLong")
    static Long getUserIdAsString(User user) {
    	return user != null && user.getId() != null ? user.getId() : null;
    }

}

