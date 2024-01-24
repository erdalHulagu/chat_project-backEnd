package com.sohbet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.MessageDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;
import com.sohbet.mapper.MessageMapper;
import com.sohbet.mapper.UserMapper;
import com.sohbet.request.SendMessageRequest;
import com.sohbet.service.MessageService;
import com.sohbet.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/messages")
public class MessageController {
	
	private MessageService messageService;
	
	private UserService userService;
	
	private UserMapper userMapper;
	
	private MessageMapper messageMapper;
	
	@Autowired
	public MessageController( MessageService messageService
			                 ,UserService userService
			                 ,UserMapper userMapper
			                 ,MessageMapper messageMapper) {
		
		this.messageService=messageService;
		this.userService=userService;
		this.userMapper=userMapper;
		this.messageMapper=messageMapper;
	}
	
	@PostMapping("/create")
	public ResponseEntity<MessageDTO>sendMessage(@RequestBody SendMessageRequest sendMessageRequest){

		UserDTO userDTO=userService.findUserProfile();
		User user =userMapper.userDTOToUser(userDTO);
		sendMessageRequest.setUserId(user.getId());
		
	MessageDTO messageDTO=	messageService.sendMessage(sendMessageRequest);
	
	return ResponseEntity.ok(messageDTO);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
		
	}
	
	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<MessageDTO>>getChatMessage(@PathVariable Long chatId){

		UserDTO userDTO=userService.findUserProfile();
		User user =userMapper.userDTOToUser(userDTO);
		
		
	List<MessageDTO> messageDTOs=	messageService.getChatMessages(chatId,user);
	
	return ResponseEntity.ok(messageDTOs);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
		
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<ChatResponse>deleteMessage(@PathVariable Long id){

		UserDTO userDTO=userService.findUserProfile();
		User user =userMapper.userDTOToUser(userDTO);
		
		messageService.deleteMessage(id,user);
		
		ChatResponse chatResponse=new ChatResponse(ResponseMessage.MESSAGE_DELETED_MESSAGE,true);
	
	return ResponseEntity.ok(chatResponse);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
		
	}
//	@DeleteMapping("/{id}")
//	public ResponseEntity<MessageDTO>getMessage(@PathVariable Long id){
//
//	Message message=	messageService.getMessageById(id);
//	
//	MessageDTO messageDTO=messageMapper.messageToMessageDTO(message);
//	
//	return ResponseEntity.ok(messageDTO);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				
//		
//	}
	

}