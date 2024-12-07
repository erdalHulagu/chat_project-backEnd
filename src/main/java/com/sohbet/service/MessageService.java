package com.sohbet.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sohbet.DTO.MessageDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;
import com.sohbet.exception.ResourceNotFoundException;
import com.sohbet.exception.message.ErrorMessage;
import com.sohbet.mapper.ChatMapper;
import com.sohbet.mapper.MessageMapper;
import com.sohbet.mapper.UserMapper;
import com.sohbet.repository.ChatRepository;
import com.sohbet.repository.MessageRepository;
import com.sohbet.repository.UserRepository;
import com.sohbet.request.SendMessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

@Service
public class MessageService {
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ChatMapper chatMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private UserRepository userRepository;


	

	// --------------- send message------------------------
	public Message sendMessage(User user, SendMessageRequest sendMessageRequest) {

		
		User usrToMessage = userService.getUserById(sendMessageRequest.getUserId());
		

		Chat chat = chatService.findChatById(sendMessageRequest.getChatId());
		
		
		Message message = new Message();
		message.setUser(usrToMessage);
		message.setChat(chat);
		message.setContent(sendMessageRequest.getContent());
		message.setCreateAt(LocalDateTime.now());
		
		user.getMessages().add(message);
		chat.getMessages().add(message);
		
		
		Message newMessage = messageRepository.save(message);
		
		return newMessage;

	}

	// --------------------get chats messages--------------------
	public List<MessageDTO> getChatMessages(Long chatId, User user) {

		Chat chat = chatService.findChatById(chatId);
		
		
		// su kontrol hata veriyor oonun icin birsey yapmalisin aksi halde herhangi bir kullanicio bu chat id ile cagirabilir
		
//		 if (!chat.getUsers().stream().anyMatch(us->us.getId().equals(user.getId()))){
//		        throw new AccessDeniedException("User does not have access to this chat");
//		    }

		Pageable pageable = PageRequest.of(0, 20); // İlk 20 mesaj
	    Page<Message> messages = messageRepository.findByChatId(chat.getId(), pageable);

	    return messageMapper.messageToMessageDTOList(messages.getContent());

		
	}

	// ------------------ get message by id-----------------

	public MessageDTO findMessageById(Long id) {
		Message message = getMessageById(id);

		MessageDTO messageDTO = messageMapper.messageToMessageDTO(message);
		return messageDTO;
	}

	// ------------------ delete message-----------------
	public void deleteMessage(Long id, User user) {
		Message message = getMessageById(id);

		if (message.getUser().getId().equals(user.getId())) {
			messageRepository.delete(message);
		}

		throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id));
	}

	// --------- method find by id-------------
	public Message getMessageById(Long id) {
		Message message = messageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.MESSAGE_NOT_FOUND, id)));

		return message;
	}

}