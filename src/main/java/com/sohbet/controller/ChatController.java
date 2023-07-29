package com.sohbet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.domain.User;
import com.sohbet.request.GroupChatRequest;
import com.sohbet.service.ChatService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController("/chats")
public class ChatController {
	
	private ChatService chatService;
	
	
	@Autowired
	public ChatController(ChatService chatService) {
		this.chatService=chatService;
	}
	
	@PostMapping
	public ResponseEntity<ChatResponse>createChat(@RequestBody User user, @Validated @PathVariable Long userId){
		
		chatService.createChat(user, userId);
		
		ChatResponse chatResponse=new ChatResponse(ResponseMessage.CHAT_CREATED_MESSAGE,true);
		
		return ResponseEntity.ok(chatResponse);
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<ChatDTO>getChatById(@PathVariable Long id){
		
	ChatDTO chatDTO=	chatService.findChatById(id);
	
	return ResponseEntity.ok(chatDTO);
		
	}
	
	@GetMapping("{userId}")
	public ResponseEntity<List<ChatDTO>> getAlluserChatsWithUserId(@PathVariable Long userId){
		
	List<ChatDTO>chatDTOs=	chatService.findAllUserChats(userId);
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	
	@PostMapping("/groupChat")
	public ResponseEntity<ChatDTO> createGroupChat(@RequestBody GroupChatRequest groupChatRequest, @RequestBody User user){
		
	ChatDTO chatDTO=	chatService.createGroup(groupChatRequest, user);
	
	return ResponseEntity.ok(chatDTO);
		
		
	}
	

}
