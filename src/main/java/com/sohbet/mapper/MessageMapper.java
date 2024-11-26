package com.sohbet.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.sohbet.DTO.MessageDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;




@Mapper(componentModel = "spring", uses = {UserMapper.class, ChatMapper.class})
public interface MessageMapper {

    @Mapping(target = "id", ignore = true) // Yeni mesaj oluşturulurken ID yok sayılır
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "chat.id", source = "chatId")
    Message messageDTOToMessage(MessageDTO messageDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "chatId", source = "chat.id")
    MessageDTO messageToMessageDTO(Message message);

    List<MessageDTO> messageToMessageDTOList(List<Message> messages);

//    @Named("getChatIdAsString")
//    static String getChatIdAsString(Chat chat) {
//        return chat != null && chat.getId() != null ? chat.getId().toString() : null;
//    }
//
//    @Named("longToString")
//    static String longToString(Long value) {
//        return value != null ? value.toString() : null;
//    }
}

