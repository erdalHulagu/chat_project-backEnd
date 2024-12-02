package com.sohbet.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.Response;
import com.sohbet.DTOresponse.ResponseMessage;
import com.sohbet.domain.Chat;
import com.sohbet.domain.User;
import com.sohbet.mapper.UserMapper;
import com.sohbet.request.GroupChatRequest;
import com.sohbet.request.SingleChatRequest;
import com.sohbet.service.ChatService;
import com.sohbet.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/chats")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;
	

	
	@Transactional
	@PostMapping("/single")
//	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANONYMOUS')")
	public ResponseEntity<ChatDTO> createSingleChat(@Valid @RequestBody SingleChatRequest singleChatRequest){
		
		ChatDTO chatDto=chatService.createChat(singleChatRequest.getUserId());
		
		return ResponseEntity.ok(chatDto);
		
	}
	
	@GetMapping("/new")
	public ResponseEntity<ChatDTO> getUserNameOfChat(@PathVariable Long chatId){
		

	ChatDTO chatDTO=chatService.getUserNameOfChat(chatId);
	
	return 	 ResponseEntity.ok(chatDTO);
	}
	
	
	@Transactional
	@GetMapping("/{id}")
	public ResponseEntity<ChatDTO>getChatById(@PathVariable Long id){
		
		ChatDTO chatDto=chatService.findChatById(id);
	return ResponseEntity.ok(chatDto);
		
	}
	
	@Transactional
	@GetMapping("/allChats")
	public ResponseEntity<List<ChatDTO>> getAlluserChatsWithUserId(){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	List<ChatDTO>chatDTOs=	chatService.findAllUserChats(user.getId());
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	@Transactional
	@PostMapping("/group")
	public ResponseEntity<ChatDTO> createGroupChat(@RequestBody GroupChatRequest groupChatRequest){
		
		UserDTO userDTO=userService.findUserProfile();
		User currentUser=userMapper.userDTOToUser(userDTO);
		
	ChatDTO chatDto=	chatService.createGroup(groupChatRequest,currentUser);
		
		return ResponseEntity.ok(chatDto);
		
	}
	
	
		
	@Transactional
	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<ChatDTO>addUserToGroup(@PathVariable Long chatId,@PathVariable Long userId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	ChatDTO chatDTOs=	chatService.addUserToGroup(userId,chatId,user);
	
	return ResponseEntity.ok(chatDTOs);
	
	}
	
	@Transactional
	@PatchMapping("/{chatId}/addAdmin/{userId}")
	public ResponseEntity<ChatDTO> addAdminToGroup( @PathVariable Long chatId, @PathVariable Long userId){
		
		UserDTO userDTO=userService .findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);
		ChatDTO chatDTOs=chatService.addAdminToChat(chatId,userId,user);
		
		return ResponseEntity.ok(chatDTOs);
	}
	
	@Transactional
	@PatchMapping("/{chatId}/removeAdmin/{userId}")
	public ResponseEntity<ChatDTO>deleteAdminFromGroup(@PathVariable Long chatId,@PathVariable Long userId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		ChatDTO chatDTO=chatService.removeAdminFromGroup(chatId,userId,user);
		
	return ResponseEntity.ok(chatDTO);
		
	}
	
	@Transactional
	@PatchMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<ChatDTO>removeUserFromGroup(@PathVariable Long chatId, @PathVariable Long userId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	ChatDTO chatDTOs=	chatService.removeUserFromGroup(userId,chatId,user);
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	@Transactional
	@DeleteMapping("/delete/{chatId}")
	public ResponseEntity<ChatResponse>deleteUserFromGroup(@PathVariable Long chatId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		chatService.deleteChat(chatId,user.getId());
		
		
		ChatResponse chatResponse=new ChatResponse(ResponseMessage.CHAT_DELETED_MESSAGE,true);
	return ResponseEntity.ok(chatResponse);
		
	}
}