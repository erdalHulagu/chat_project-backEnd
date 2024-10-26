package com.sohbet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
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

import jakarta.validation.Valid;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository; // 23528429-6870-4b91-b125-6f18af9d23de 2e7f20af-ddfe-451f-8b24-5a2ee3c19d59
											// 2fe789b6-1cde-4832-80b7-f52b7d52c6e2
	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ChatMapper chatMapper;

	@Autowired
	private ImageService imageService;

//	
//	public ChatService(ChatRepository chatRepository, UserService userService, UserMapper userMapper,
//			ChatMapper chatMapper) {
//		this.chatRepository = chatRepository;
//		this.userService = userService;
//		this.userMapper = userMapper;
//		this.chatMapper = chatMapper;
//	}
//
	public ChatDTO createSingleChat(Long userId) {

		User currentUser = userService.getCurrentUser();
		User user = userService.getUser(userId);

		Chat isChatExist = chatRepository.findSingleChatByUserIds(currentUser, user);

		if (isChatExist != null) {
			ChatDTO chatDTO = chatMapper.chatToChatDTO(isChatExist);

			return chatDTO;

		}

		Set<User> users = new HashSet<>();
		users.add(user);
		users.add(currentUser);
		
		Set<User> adminSet=new HashSet<>();
		adminSet.add(currentUser);

		Chat chat = new Chat();
		chat.setCreatedBy(currentUser);
		chat.setAdmins(adminSet);
		chat.setUsers(users);
		chat.setIsGroup(false);

		 chatRepository.save(chat);
		return   chatMapper.chatToChatDTO(chat);
//		return chatRepository.save(chat);
//		Chat newChat = chatRepository.save(chat);
//		return chatMapper.chatToChatDTO(newChat);

	}

	// -------------------- create chat with user-----------------
	public ChatDTO createChat(Long userId) {

	    UserDTO currentUserDTO = userService.findUserProfile();
	    User currentUser = userMapper.userDTOToUser(currentUserDTO);

	    User user = userService.getUser(userId);
	    
	    
	    
	    

	    Chat isChatExist = chatRepository.findSingleChatByUserIds(currentUser, user);

	    if (isChatExist != null) {
	        ChatDTO chatDTO = chatMapper.chatToChatDTO(isChatExist);
	        return chatDTO;
	    }
	
	    Chat newChat = new Chat();
	    newChat.setCreatedBy(currentUser);
	    newChat.setIsGroup(false);
      if (currentUser.getId().equals(user.getId())) {
	    	
	    	newChat.getAdmins().add(currentUser);
	    	
		}
	  

	    // Chat name kontrolü
	    if (newChat.getChatName() == null || newChat.getChatName().isEmpty()) {
	        newChat.setChatName(currentUser.getFirstName() + "-" + user.getFirstName());
	    }

	    chatRepository.save(newChat);

	    return chatMapper.chatToChatDTO(newChat);
	}
//	// -------------------- create chat with user-----------------
//	public ChatDTO createChat(Long userId) {
//		
//		UserDTO currentUserDTO = userService.findUserProfile();
//		User currentUser = userMapper.userDTOToUser(currentUserDTO);
//		
//		User user = userService.getUser(userId);
//		
//		Chat isChatExist = chatRepository.findSingleChatByUserIds(currentUser, user);
//		
//		if (isChatExist != null) {
//			ChatDTO chatDTO = chatMapper.chatToChatDTO(isChatExist);
//			
//			return chatDTO;
//			
//		}
//		
//		
//		Set<User> users = new HashSet<>();
//		users.add(user);
//		users.add(currentUser);
//		
//		
//		Set<User> adminSet=new HashSet<>();
//		adminSet.add(currentUser);
//		
//		Chat chat = new Chat();
//		
//		chat.setCreatedBy(currentUser);
//		chat.setAdmins(adminSet);
//		chat.setUsers(users);
//		chat.setIsGroup(false);
//		
//		Chat newChat = chatRepository.save(chat);
//		return chatMapper.chatToChatDTO(chat);
//		
//	}

	// ----------- find chat by id---------------
	
	public ChatDTO findChatById(Long id) {
		Chat chat = findById(id);
		ChatDTO chatDTO = chatMapper.chatToChatDTO(chat);
		return chatDTO;
	}

	// ---------------- get all user chats------------
	public List<ChatDTO> findAllUserChats(Long userId) {

		List<Chat> chats = chatRepository.findChatByUserId(userId);
		List<ChatDTO> chatDTOs = chatMapper.mapChatListToChatDTOList(chats);

		return chatDTOs;
	}

	// ---------------- createGroup chat------------------ BUNA KADAR CONTROLLER
	// YAPILDI
	public ChatDTO createGroup(GroupChatRequest groupChatRequest, User user) {
		

		Chat chat = new Chat();
//		Set<Image> image = stringToImageMap(groupChatRequest.getChatImage()); // we called stringToImageMap for string
//																		// image to set Strin image that we
//																				// created in this class
		chat.setIsGroup(true);
//		chat.setChatImage(image);
		chat.setChatName(groupChatRequest.getChatName());
		chat.setCreatedBy(user);
		chat.getAdmins().add(user);

		for (Long userId : groupChatRequest.getUserIds()) {

			UserDTO userDTO = userService.getUserById(userId);
			User usr = userMapper.userDTOToUser(userDTO);
			chat.getUsers().add(usr);
		}
		List<String>groupUsers=new ArrayList<>();
		for(Long id:groupChatRequest.getUserIds()) {
			
			UserDTO userDTO = userService.getUserById(id);
			User usr = userMapper.userDTOToUser(userDTO);
			
			groupUsers.add(usr.getFirstName());
			
			
			
		}
		String joinedFirstNames = String.join("-", groupUsers);
			
		
		 if (chat.getChatName() == null ||chat.getChatName().isEmpty()) {
		        chat.setChatName(user.getFirstName() + "-(" + joinedFirstNames+") groupChat");
		    }
		 chatRepository.save(chat);

		    
		ChatDTO chatDTO = chatMapper.chatToChatDTO(chat);
		return chatDTO;

	}

	// -----------add user to group-------------------
	public ChatDTO addUserToGroup(Long userId, Long ChatId, User user) {
		Chat chat = findById(ChatId);
		UserDTO userDTO = userService.getUserById(userId);
		User userAdd = userMapper.userDTOToUser(userDTO);
		
		if (!chat.getAdmins().contains(user)) {
			throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
			
		}
		chat.getUsers().add(userAdd);

		chatRepository.save(chat);
		ChatDTO chatDTO2 = chatMapper.chatToChatDTO(chat);

		return chatDTO2;
		

	}

	// -----------------rename group---------
	public ChatDTO renameGroup(Long id, String goupName, User user) {

		ChatDTO chatDTO = findChatById(id);
		Chat chat = chatMapper.chatDTOToChat(chatDTO);

		if (chat.getUsers().contains(user)) {
			chat.setChatName(goupName);
			Chat cht = chatRepository.save(chat);
			ChatDTO chatDto1 = chatMapper.chatToChatDTO(cht);
			return chatDto1;
		}
		throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	}

	// ----------------------remove chat group -------------
	public ChatDTO removeUserFromGroup(Long userId, Long chatId, User user) {

	    ChatDTO chatDTO = findChatById(chatId);
	    Chat chat = chatMapper.chatDTOToChat(chatDTO);

	    UserDTO userDTO = userService.getUserById(userId);
	    User userToRemove = userMapper.userDTOToUser(userDTO);

//	    boolean isAdmin = chat.getAdmins().stream().anyMatch(admin -> admin.getId().equals(user.getId()));
	    boolean isItSameUser = userToRemove.getId().equals(user.getId());

	    // Admin veya aynı kullanıcı değilse çıkarabilir
	    if ( !isItSameUser) {
	    	
	        chat.getUsers().remove(userToRemove);
	        Chat updatedChat = chatRepository.save(chat);
	        return chatMapper.chatToChatDTO(updatedChat);
	    }

	    // Eğer admin değilse ve aynı kullanıcı ise hata fırlatılır
	    throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	}


	// ----------- deleteChat-----------------
	public void deleteChat(Long chatId, Long userId) {

		ChatDTO chatDTO = findChatById(chatId);
		Chat chat = chatMapper.chatDTOToChat(chatDTO);
		chatRepository.deleteById(chat.getId());

	}
	
	//---------- CLASS METHODS BELOWE------------------------

	// ---------------------stringToImage---------------------
	public Set<Image> stringToImageMap(Set<String> imageUrls) {
		Set<Image> images = new HashSet<>();
		for (String imageUrl : imageUrls) {
			Image image = new Image();
			image.setId(imageUrl);
									
			images.add(image);
		}
		return images;
	}

	// ------------ get image by string id ------------------ extra
	public Image getImage(String id) {
		Image imageFile = imageService.getImageById(id);
		return imageFile;
	}

	public Chat findById(Long id) {
		Chat chat = chatRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.CHAT_NOT_FOUND_MESSAGE, id)));
		return chat;
	}

}