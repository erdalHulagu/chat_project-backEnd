package com.sohbet.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.DTO.UserDTO;
import com.sohbet.DTOresponse.Response;
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
import com.sohbet.repository.UserRepository;
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

	@Autowired
	private UserRepository userRepository;

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
		User user = userService.getUserById(userId);

		Chat isChatExist = chatRepository.findSingleChatByUserIds(currentUser, user);

		if (isChatExist != null) {
			ChatDTO chatDTO = chatMapper.chatToChatDTO(isChatExist);

			return chatDTO;

		}

		Set<User> users = new HashSet<>();
		users.add(user);
		users.add(currentUser);

		Set<User> adminSet = new HashSet<>();
		adminSet.add(currentUser);

		Chat chat = new Chat();
		chat.setCreatedBy(currentUser);
		chat.setAdmins(adminSet);
		chat.getUsers().addAll(users);
		chat.setIsGroup(false);
		

	Chat newChat=chatRepository.save(chat);
		return chatMapper.chatToChatDTO(newChat);

	}

	// -------------------- create chat with user-----------------
	public ChatDTO createChat(Long userId) {

		UserDTO currentUserDTO = userService.findUserProfile();
		User currentUser = userMapper.userDTOToUser(currentUserDTO);

		User user = userService.getUserById(userId);

		Chat isChatExist = chatRepository.findSingleChatByUserIds(currentUser, user);

		if (isChatExist != null) {
			ChatDTO chatDTO = chatMapper.chatToChatDTO(isChatExist);
			return chatDTO;
		}

		Chat chat = new Chat();
		chat.setCreatedBy(currentUser);
		chat.setIsGroup(false);
		
		if (currentUser.getId().equals(user.getId())) {

			chat.getAdmins().add(currentUser);

		}
		

		// Chat name kontrolü
		if (chat.getChatName() == null || chat.getChatName().isEmpty()) {
			chat.setChatName(currentUser.getFirstName() + "-" + user.getFirstName());
		}

		chatRepository.save(chat);
	
		return chatMapper.chatToChatDTO(chat);
	}

	// ----------- find chat by id---------------

	public Chat findChatById(Long id) {
		Chat chat = chatRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.CHAT_NOT_FOUND_MESSAGE, id)));
		return chat;
	}

	public ChatDTO findById(Long id) {
		Chat chat = findChatById(id);
		ChatDTO chatDTO = chatMapper.chatToChatDTO(chat);
		return chatDTO;
	}

	// ---------------- get all user chats------------
	public List<ChatDTO> findAllUserChats(Long userId) {

		List<Chat> chats = chatRepository.findChatByUserId(userId);
		List<ChatDTO> chatDTOs = chatMapper.chatListToChatDTOList(chats);

		return chatDTOs;
	}

	// ---------------- createGroup chat------------------

	public ChatDTO createGroup(GroupChatRequest groupChatRequest, User user) {

		// got profile imaga
		Image groupProfileImage = imageService.getImageById(groupChatRequest.getChatImage());

		if (groupProfileImage == null) {
			return null;

		}

		// Yeni bir Chat nesnesi oluştur
		Chat chat = new Chat();
		chat.setIsGroup(true);
		chat.setChatProfileImage(groupProfileImage); // set groupProfileImage

		// Chat adı ve kullanıcıları ayarla

		chat.setChatName(groupChatRequest.getChatName());
		chat.setCreatedBy(user);
		chat.getAdmins().add(user);
		chat.getUsers().add(user);
		
		

		// Kullanıcıların adlarını ve ID'lerini tek bir döngüde işleyelim

		List<String> groupUsers = new ArrayList<>();

		for (Long userId : groupChatRequest.getUserIds()) {
			User usr = userService.getUserById(userId);

			chat.getUsers().add(usr);
			groupUsers.add(usr.getFirstName());

		}

		// Eğer chatName boş ise, kullanıcı adlarıyla dinamik bir isim oluştur
		if (chat.getChatName() == null || chat.getChatName().isEmpty()) {
			String joinedFirstNames = String.join("-", groupUsers);
			chat.setChatName(user.getFirstName() + "-(" + joinedFirstNames + ") groupChat");
		}

		// Chat nesnesini kaydet
		Chat savedChat = chatRepository.save(chat);

		// DTO'ya dönüştür ve geri döndür
		return chatMapper.chatToChatDTO(savedChat);
	}

	// -----------add user to group-------------------
	public ChatDTO addUserToGroup(Long userId, Long chatId, User user) {
		Chat chat = findChatById(chatId);
		User userAdd = userService.getUserById(userId);
		boolean isAdmin = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(user.getId()));
		boolean isUser = chat.getUsers().stream().anyMatch(usr -> usr.getId().equals(userAdd.getId()));

		if (!isAdmin) {
			throw new BadRequestException(String.format(ErrorMessage.NO_PERMISSION_MESSAGE, user.getFirstName()));

		} else if (isUser) {
			throw new BadRequestException(String.format(ErrorMessage.THIS_USER_ALREADY_EXIST, userAdd.getFirstName()));

		}

		chat.getUsers().add(userAdd);

		chatRepository.save(chat);
		ChatDTO chatDTO2 = chatMapper.chatToChatDTO(chat);

		return chatDTO2;

	}

	// -------------- add admin to group ------------------

	public ChatDTO addAdminToGroupChat(Long chatId, Long userId, User user) {
		Chat chat = findChatById(chatId);

		User userAddAdmin = userService.getUserById(userId);

		boolean isAdmin = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(user.getId()));
		boolean IsUserAddAdmin = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(userAddAdmin.getId()));

		if (!isAdmin) {
			throw new BadRequestException(String.format(ErrorMessage.NO_PERMISSION_MESSAGE, user.getFirstName()));

		} else if (IsUserAddAdmin) {
			throw new BadRequestException(
					String.format(ErrorMessage.THIS_USER_ALREADY_ADMIN, userAddAdmin.getFirstName()));

		} else if (!chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(userAddAdmin.getId()))) {
			chat.getUsers().add(userAddAdmin);
		}

		// Add the new admin and save the chat
		chat.getAdmins().add(userAddAdmin);
		Chat savedChat = chatRepository.save(chat);
		return chatMapper.chatToChatDTO(savedChat);
	}

	public ChatDTO removeAdminFromGroup(Long chatId, Long userId, User user) {
		Chat chat = findChatById(chatId);

		User adminToRemove = userService.getUserById(userId);

		boolean isAdmin = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(user.getId()));
		boolean isAdminToRemove = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(adminToRemove.getId()));
		boolean isUserTrue = chat.getUsers().stream().anyMatch(usr -> usr.getId().equals(adminToRemove.getId()));
		if (!isAdmin) {
			throw new BadRequestException(String.format(ErrorMessage.NO_PERMISSION_MESSAGE, user.getFirstName()));

		} else if (!isAdminToRemove) {
			throw new BadRequestException(String.format(ErrorMessage.THIS_USER_IS_NOT_ADMIN, user.getFirstName()));

		} else if (!isUserTrue) { // Check if userAddAdmin is in users
			chat.getUsers().add(adminToRemove);

		}
		chat.getAdmins().remove(adminToRemove);
		Chat savedChat = chatRepository.save(chat);
		return chatMapper.chatToChatDTO(savedChat);

	}

	// -----------------rename group---------
	public ChatDTO renameGroup(Long id, String goupName, User user) {

		Chat chat = findChatById(id);

		boolean isUserTrue = chat.getUsers().stream().anyMatch(usr -> usr.getId().equals(user.getId()));
		if (isUserTrue) {
			chat.setChatName(goupName);
			Chat cht = chatRepository.save(chat);
			ChatDTO chatDto1 = chatMapper.chatToChatDTO(cht);
			return chatDto1;
		}
		throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);
	}

	// ----------------------remove group chat------------- BURAYA KADAR KONTROL
	// EDILDI
	public ChatDTO removeUserFromGroup(Long userId, Long chatId, User user) {

		Chat chat = findChatById(chatId);

		User userToRemove = userService.getUserById(userId);

		// Admin veya aynı kullanıcı değilse çıkarabilir

		boolean isAdmin = chat.getAdmins().stream().anyMatch(usr -> usr.getId().equals(user.getId()));

		if (!isAdmin || user.getId().equals(userToRemove.getId())) {
			// Eğer admin değilse ve aynı kullanıcı ise hata fırlatılır
			throw new BadRequestException(ErrorMessage.NO_PERMISSION_MESSAGE);

		}
		chat.getUsers().remove(userToRemove);
		Chat updatedChat = chatRepository.save(chat);
		return chatMapper.chatToChatDTO(updatedChat);

	}

	// ----------- deleteChat-----------------
	public void deleteChat(Long chatId, Long userId) {

		Chat chat = findChatById(chatId);
		chatRepository.deleteById(chat.getId());

	}

	// ----------- get user by chat id-----------------
	public Set<UserDTO> getChatUsersByChatId(Long chatId) {
		Chat chat = chatRepository.findById(chatId).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.CHAT_NOT_FOUND_MESSAGE, chatId)));
		return chat.getAdmins().stream().map(userMapper::userToUserDto).collect(Collectors.toSet());
	}

	// ---------- CLASS METHODS BELOWE------------------------

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

	public Set<Image> mapStringToImage(Set<String> ids) {
		Set<Image> images = new HashSet<>();
		for (String id : ids) {
			Image image = new Image();
			image.setId(id);
			images.add(image);
		}
		return images;
	}

}