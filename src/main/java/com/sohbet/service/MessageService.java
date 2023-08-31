package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.MessageDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;
import com.sohbet.exception.BadRequestException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.ChatMapper;
import com.sohbet.mapper.MessageMapper;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.MessageRepository;
import com.sohbet.request.SendMessageRequest;

@Service
public class MessageService {
	
	
	private MessageRepository messageRepository;
	
	private UserService userService;
	
	private ChatService chatService;
	
	private UserMapper userMapper;
	
	private ChatMapper chatMapper;
	
	private MessageMapper messageMapper;
	
	
	@Autowired
	public MessageService(UserService userService,ChatService chatService
			                                     ,MessageRepository messageRepository
			                                     ,ChatMapper chatMapper
			                                     ,UserMapper userMapper
			                                     ,MessageMapper messageMapper) {
		
		this.userService=userService;
		this.chatService=chatService;
		this.messageRepository=messageRepository;
		this.chatMapper=chatMapper;
		this.userMapper=userMapper;
		this.messageMapper=messageMapper;
		
	}
	
	//--------------- send message------------------------
	public MessageDTO sendMessage(SendMessageRequest sendMessageRequest) {
		
		UserDTO userDTO =userService.getUserById(sendMessageRequest.getUserId());
		User user=userMapper.userDTOToUser(userDTO);
		
		ChatDTO chatDTO=chatService.findChatById(sendMessageRequest.getUserId());
		Chat chat=chatMapper.chatDTOToChat(chatDTO);
		
		Message message= new Message();		
		message.setUser(user);
		message.setChat(chat);
		message.setContent(sendMessageRequest.getContent());
		message.setCreateAt(LocalDateTime.now());
		
		MessageDTO messageDTO=messageMapper.messageToMessageDTO(message);
		return messageDTO;
		
	}
	 
	//--------------------get chats messages--------------------
	public List<MessageDTO> getChatMessages(Long chatId,User user){
		
		ChatDTO chatDTO=chatService.findChatById(chatId);
		
		Chat chat = chatMapper.chatDTOToChat(chatDTO);
		
		if(!chat.getUsers().contains(user)) {
			throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
		}
		List<Message> messages=messageRepository.findByChatId(chat.getId());
		
	    List<MessageDTO>messageDTOs=messageMapper.messageToMessageDTOList(messages);
			
		return messageDTOs;
	}
	
	//------------------ get message by id-----------------
	
	public MessageDTO findMessageById(Long id) {
	Message message=getMessageById(id);
	
	MessageDTO messageDTO=messageMapper.messageToMessageDTO(message);
	return messageDTO;
	}
	
	//------------------ delete message-----------------
	public void deleteMessage(Long id, User user) {
		Message message=getMessageById(id);
		
		
		if (message.getUser().getId().equals(user.getId())) {
			messageRepository.delete(message);
		}
		
		throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id));
		}
	
	//--------- method find by id-------------
	public Message getMessageById(Long id) {
		Message message=	messageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.MESSAGE_NOT_FOUND, id)));
		
		return message;
		}
	
}