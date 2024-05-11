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

@Mapper(componentModel = "spring")
public interface ChatMapper {
	
	
	@Mapping(target = "chatImage",source = "chatImage",qualifiedByName = "getImageAsString")
	
    ChatDTO chatToChatDTO (Chat chat);
 
 
     List<ChatDTO> mapChatListToChatDTOList(List<Chat> chatList);

    @Mapping(target = "id", ignore = true)
 	@Mapping(target = "chatImage",source = "chatImage",qualifiedByName = "getImageStringAsImage")
     Chat chatDTOToChat (ChatDTO chatDTO);

     @Named("getImageAsString")
     
    public static  Set<String> getImageIds( Set<Image> imageFiles) {
	Set<String> imgs = new HashSet<>();
	imgs = imageFiles.stream().map(imFile->imFile.getId().
															toString()).
															collect(Collectors.toSet());
	 return imgs;
}
     @Named("getImageStringAsImage")
     public static Set<Image> map(Set<String> imageUrls) {
  		 Set<Image> images = new HashSet<>();
  	        for (String imageUrl : imageUrls) {
  	            Image image = new Image();
  	            image.setId(imageUrl);   // Eğer Image sınıfında başka alanlar varsa, diğer alanları da ayarlayabilirsiniz
  	            images.add(image);
  	        }
  	        return images;
  	 }
}