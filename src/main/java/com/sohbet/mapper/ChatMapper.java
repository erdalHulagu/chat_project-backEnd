package com.sohbet.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.sohbet.DTO.ChatDTO;
import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;

@Mapper(componentModel = "spring")
public interface ChatMapper {

	@Mapping(target = "createdBy", source = "createdBy.id") // User'dan username alarak atama yap
	@Mapping(target = "admin", source = "admin", qualifiedByName = "getUserCollectionAsString")
	@Mapping(target = "messages", source = "messages", qualifiedByName = "getMessageCollectionAsString")
	@Mapping(target = "users", source = "users", qualifiedByName = "getUserCollectionAsString")
	@Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "getImageAsString")
	ChatDTO chatToChatDTO(Chat chat);

	List<ChatDTO> mapChatListToChatDTOList(List<Chat> chatList);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "admin", ignore = true) // Admin seti ayrı bir işlemde setlenebilir
	@Mapping(target = "messages", ignore = true) // Messages seti ayrı bir işlemde setlenebilir
	@Mapping(target = "users", ignore = true) // Users seti ayrı bir işlemde setlenebilir
	@Mapping(target = "createdBy", ignore = true) // CreatedBy ayrı bir işlemde setlenebilir
	@Mapping(target = "chatImage", source = "chatImage", qualifiedByName = "getImageStringAsImage")
	Chat chatDTOToChat(ChatDTO chatDTO);
	
	
	
	
//----------------------------
	@Named("getImageAsString")

	public static Set<String> getImageIds(Set<Image> imageFiles) {
		Set<String> imgs = new HashSet<>();
		imgs = imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
		return imgs;
	}
	
	
	
	// ---------------------------------
	@Named("getImageStringAsImage")
	public static Set<Image> map(Set<String> imageUrls) {
		Set<Image> images = new HashSet<>();
		for (String imageUrl : imageUrls) {
			Image image = new Image();
			image.setId(imageUrl); // Eğer Image sınıfında başka alanlar varsa, diğer alanları da
									// ayarlayabilirsiniz
			images.add(image);
		}
		return images;
	}

	
	
	// ---------------------
	@Named("getUserCollectionAsString")
	public static Set<String> getUserCollectionAsString(Set<User> users) {
		return users.stream().map(user -> user.getId().toString()).collect(Collectors.toSet());
	}

	
	//------------------------------
	@Named("getMessageCollectionAsString")
	public static List<String> getMessageCollectionAsString(List<Message> messages) {
		return messages.stream().map(message -> message.getId().toString()).collect(Collectors.toList());
	}

}