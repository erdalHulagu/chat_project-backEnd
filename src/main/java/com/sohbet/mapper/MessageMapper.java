package com.sohbet.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sohbet.DTO.MessageDTO;
import com.sohbet.domain.Message;


@Mapper(componentModel = "spring")
public interface MessageMapper {
	
	@Mapping(target = "id", ignore = true)
	Message messageDTOToMessage(MessageDTO messageDTO);
	
	List<MessageDTO> messageToMessageDTOList(List<Message> messages);
	
	
	MessageDTO messageToMessageDTO(Message message);

}
