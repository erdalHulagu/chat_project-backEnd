package com.sohbet.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.sohbet.DTO.MessageDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;


@Mapper(componentModel = "spring")
public interface MessageMapper {
	
	@Mapping(target = "id", ignore = true)
	Message messageDTOToMessage(MessageDTO messageDTO);
	
	List<MessageDTO> messageToMessageDTOList(List<Message> messages);
	

	MessageDTO messageToMessageDTO(Message message);
	
	
	 @Named("getChatIdAsString")
	    public static String getChatIdAsString(Chat chat) {
	        return chat.getId().toString();
	    }
	 
	 @Named("longToString")
	    public static String longToString(Long value) {
	        return value != null ? value.toString() : null;
	    }

}
