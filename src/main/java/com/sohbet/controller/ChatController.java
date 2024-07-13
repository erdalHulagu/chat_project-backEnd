package com.sohbet.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;
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
	
//	
//	public ChatController(ChatService chatService, UserService userService, UserMapper userMapper) {
//		this.chatService=chatService;
//		this.userService=userService;
//		this.userMapper=userMapper;
//		
//	}
	
//	@PostMapping("/dummy")
//	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANONYMOUS')")
//	public ResponseEntity<ChatDTO> dummy(){
//	
//		User currentUser= userService.getCurrentUser();
//	ChatDTO chatDTO= chatService.createDummyChat(currentUser);
//	
//	
//	return ResponseEntity.ok(chatDTO);
//	
//		
//	}
	

	 @Transactional
	@PostMapping("/single/{id}")
//	@PreAuthorize("hasRole('ADMIN') or hasRole('ANONYMOUS')")
	public ResponseEntity<ChatDTO> createSingleChat(@PathVariable Long id){
		
		ChatDTO chatDTO=chatService.createChat(id);
		
//		Response response= new Response(ResponseMessage.CHAT_DUMMY_SUCCESFULL,true);
		
		return ResponseEntity.ok(chatDTO);
		
	}
	
	@PostMapping("/singleChat")
//	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANONYMOUS')")
	public ResponseEntity<ChatDTO> createSingleChat(@Valid @RequestBody SingleChatRequest singleChatRequest){
		
		    
	
		ChatDTO chatDto=chatService.createSingleChat(singleChatRequest.getUserId());
		
		return ResponseEntity.ok(chatDto);
		
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ChatDTO>getChatById(@PathVariable Long id){
		
		ChatDTO chatDto=chatService.findChatById(id);
	return ResponseEntity.ok(chatDto);
		
	}
	
	
	@GetMapping("/allChats")
	public ResponseEntity<List<ChatDTO>> getAlluserChatsWithUserId(){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	List<ChatDTO>chatDTOs=	chatService.findAllUserChats(user.getId());
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	
	@PostMapping("/group")
	public ResponseEntity<ChatDTO> createGroupChat(@RequestBody GroupChatRequest groupChatRequest){
		
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);
		
	ChatDTO chatDto=	chatService.createGroup(groupChatRequest,user);
		
		return ResponseEntity.ok(chatDto);
		
	}
	
	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<ChatDTO>addUserToGroup(@PathVariable Long id,@PathVariable Long userId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	ChatDTO chatDTOs=	chatService.addUserToGroup(userId,id,user);
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	
	@PutMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<ChatDTO>removeUserFromGroup(@PathVariable Long id,@PathVariable Long userId){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		
	ChatDTO chatDTOs=	chatService.removeGroup(userId,id,user);
	
	return ResponseEntity.ok(chatDTOs);
		
	}
	@DeleteMapping("/delete/{chatId}")
	public ResponseEntity<ChatResponse>deleteUserFromGroup(@PathVariable Long id){
		UserDTO userDTO=userService.findUserProfile();
		User user=userMapper.userDTOToUser(userDTO);	
		chatService.deleteChat(id,user.getId());
		
		
		ChatResponse chatResponse=new ChatResponse(ResponseMessage.CHAT_DELETED_MESSAGE,true);
	return ResponseEntity.ok(chatResponse);
		
	}
}