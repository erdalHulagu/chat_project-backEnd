package com.sohbet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.User;
import com.sohbet.enums.RoleType;
import com.sohbet.exception.BadRequestException;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.ChatMapper;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.ChatRepository;
import com.sohbet.request.AdminUserUpdateRequest;
import com.sohbet.request.GroupChatRequest;

@Service
public class ChatService {
	
     private ChatRepository chatRepository;
     
     private  UserService userService;
     
     private  UserMapper userMapper;
     
     private ChatMapper chatMapper;
     
     @Autowired
    public ChatService(ChatRepository chatRepository,UserService userService,UserMapper userMapper,ChatMapper chatMapper) {
    	this.chatRepository=chatRepository;
    	this.userService=userService;
    	this.userMapper=userMapper;
    	this.chatMapper=chatMapper;
	}
	
     //-------------------- create chat with user-----------------
	public ChatDTO createChat (User user, Long userId ) {
	UserDTO userDto=	userService.getUserById(userId);
    User usr=userMapper.userDTOToUser(userDto);
	
	Chat isChatExist= chatRepository.findSingleChatByUserIds(user, userId);
	
	if (isChatExist!=null) {                                                       
	ChatDTO chatDTO=	chatMapper.chatToChatDTO(isChatExist);
		
		return chatDTO;
		
	}

	Chat chat =new Chat();
	chat.setCreatedBy(user);
	chat.getUsers().add(usr);
	chat.getUsers().add(user);
	chat.setIsGroup(false);

	ChatDTO chatDTO=chatMapper.chatToChatDTO(chat);
	return chatDTO;
	}

	//----------- find chat by id---------------
	public ChatDTO findChatById(Long id) {
	Chat chat=	chatRepository.findById(id).orElseThrow(()-> 
	                              new ResourceNotFoundException(String.format(ErrorMessage.CHAT_NOT_FOUND_MESSAGE, id)));
	ChatDTO chatDTO=	chatMapper.chatToChatDTO(chat);
		return chatDTO ;
	}
	
	//---------------- get all user chats------------
	public List<ChatDTO> findAllUserChats(Long userId){
		
	UserDTO userDto=	userService.getUserById(userId);
	User user=userMapper.userDTOToUser(userDto);
	
	List<Chat> chats=chatRepository.findChatByUserId(user.getId());
List<ChatDTO> chatDTOs=	chatMapper.mapChatListToChatDTOList(chats);
	
	return chatDTOs;
	}
	
	
	//---------------- createGroup chat------------------ BUNA KADAR CONTROLLER YAPILDI
	public ChatDTO createGroup(GroupChatRequest groupChatRequest, User user) {
		
		Chat chat= new Chat();
		
		chat.setIsGroup(true);
		chat.setChatImage(groupChatRequest.getChatImage());
		chat.setChatName(groupChatRequest.getChatName());
		chat.setCreatedBy(user);
		chat.getAdmin().add(user);
		
		for (Long userId : groupChatRequest.getUserIds()) {
			
			UserDTO userDTO=userService.getUserById(userId);
		User usrs=	userMapper.userDTOToUser(userDTO);
			chat.getUsers().add(usrs);
		}
	  ChatDTO chatDTO= chatMapper.chatToChatDTO(chat);
		return chatDTO;
		
	}
	
	//-----------add user to group-------------------
	public ChatDTO addUserToGroup(Long userId, Long id, User user) {
		ChatDTO chatDTO = findChatById(id);
		Chat chat =chatMapper.chatDTOToChat(chatDTO);
	
		
		UserDTO userDTO=userService.getUserById(userId);
		User user1=userMapper.userDTOToUser(userDTO);
	if (chat.getAdmin().contains(user)) {
		chat.getUsers().add(user1);
		
		chatRepository.save(chat);
		ChatDTO chatDTO2 =chatMapper.chatToChatDTO(chat);
		
		return chatDTO2;
	}
	throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
		
	
		
	}
	
	
	//-----------------rename group---------
	public ChatDTO renameGroup (Long id, String goupName, User user) {
		
		ChatDTO chatDTO = findChatById(id);
		Chat chat =chatMapper.chatDTOToChat(chatDTO);
		
		if(chat.getUsers().contains(user)) {
			chat.setChatName(goupName);
			Chat cht =chatRepository.save(chat);
			ChatDTO chatDto1 = chatMapper.chatToChatDTO(cht);
			return chatDto1;
		}
		throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	}
	
	//----------------------remove chat group -------------
   public ChatDTO removeGroup (Long id, Long userId, User user) {
		
	     ChatDTO chatDTO = findChatById(id);
	    Chat chat =chatMapper.chatDTOToChat(chatDTO);

	
	       UserDTO userDTO=userService.getUserById(userId);
	    User user1=userMapper.userDTOToUser(userDTO);
      if (chat.getAdmin().contains(user)||user1.getId().equals(user.getId())) {
	        chat.getUsers().remove(user1);
	
	       chatRepository.save(chat);
	        ChatDTO chatDTO2 =chatMapper.chatToChatDTO(chat);
	
	            return chatDTO2;
}
          throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	

	
}
   //----------- deleteChat-----------------
   public void deleteChat (Long id, Long userId) {
		
	     ChatDTO chatDTO = findChatById(id);
	    Chat chat =chatMapper.chatDTOToChat(chatDTO);
	    chatRepository.deleteById(chat.getId());

   }

}
