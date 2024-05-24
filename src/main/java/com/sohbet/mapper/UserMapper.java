package com.sohbet.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.sohbet.DTO.UserDTO;
import com.sohbet.domain.Image;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;
import com.sohbet.request.RegisterRequest;
import com.sohbet.request.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createAt", ignore = true)
	@Mapping(target = "updateAt", ignore = true)
	@Mapping(target = "chat", ignore = true)
	@Mapping(target = "chats", ignore = true)
	@Mapping(target = "friends", ignore = true)
	@Mapping(target = "messages", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
//	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
//	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "getImageAsByte")
	User userDTOToUser(UserDTO userDTO);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createAt", ignore = true)
	@Mapping(target = "updateAt", ignore = true)
	@Mapping(target = "chat", ignore = true)
	@Mapping(target = "chats", ignore = true)
	@Mapping(target = "friends", ignore = true)
	@Mapping(target = "messages", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
//	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
//	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "getImageAsByte")
	User registerUserToUser(RegisterRequest registerRequest);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createAt", ignore = true)
	@Mapping(target = "updateAt", ignore = true)
	@Mapping(target = "chat", ignore = true)
	@Mapping(target = "chats", ignore = true)
	@Mapping(target = "friends", ignore = true)
	@Mapping(target = "messages", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
//	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsImage")
//	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "getImageAsByte")
	User userRequestToUser(UserRequest userRequest);

	
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
//	@Mapping(target = "myImages", source = "myImages", qualifiedByName = "getImageCollectionAsString")
//	@Mapping(target = "profileImage", source = "profileImage", qualifiedByName = "getImageAsString")
	UserDTO userToUserDto(User user);

	List<UserDTO> userToUserDTOList(List<User> userList);

	@Named("getRoleAsString")
	public static Set<String> mapRoles(Set<Role> roles) {

		Set<String> roleStr = new HashSet<>();

		roles.forEach(r -> {
			roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek

		});

		return roleStr;
	}

	@Named("getImageAsByte")
	public static Image singleImage1(String imgId) {
		
		Image image = new Image();
		
		image.setType(imgId);

		return image;

	}
//	@Named("getImageAsImage")
//	public static BufferedImage singleImage(String image) throws IOException {
//	    // Base64 stringini byte dizisine dönüştürme
//	    byte[] imageBytes = Base64.getDecoder().decode(image);
//	    
//	    // ByteArrayInputStream kullanarak byte dizisini BufferedImage nesnesine dönüştürme
//	    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
//	    BufferedImage img = ImageIO.read(bais);
//	    
//	    return img;
//	}
	@Named("getImageAsString")
	public static byte[] singleStringImage(Image image) {
		
	byte[] imgbyte=image.getImageData().getData();
	
	return imgbyte;
		
	}

	@Named("getImageCollectionAsImage")
	public static Set<Image> mapping(Set<String> imageUrls) {
		Set<Image> images = new HashSet<>();
		for (String imageUrl : imageUrls) {
			Image image = new Image();
			image.setId(imageUrl); // Varsayılan olarak sadece id atanıyor

			// Diğer alanlar da varsa onları da ayarlayabilirsiniz
			// Örneğin:
			// image.setName("Image Name"); // İsim belirlenmesi gerekiyorsa
			// image.setType("jpg"); // Tip belirlenmesi gerekiyorsa
			// image.setData(yourImageData); // Resim verisi atanması gerekiyorsa

			images.add(image);
		}
		return images;
	}

	// long turunde image i image turunde image e cevidik
//	 @Named("getImageAsLong")
//   public static Set<Image> map(Set<String> imageUrls) {
//		 Set<Image> images = new HashSet<>();
//	        for (String imageUrl : imageUrls) {
//	            Image image = new Image();
//	            image.setId(imageUrl);   // Eğer Image sınıfında başka alanlar varsa, diğer alanları da ayarlayabilirsiniz
//	            images.add(image);
//	        }
//	        return images;
//	 }
	@Named("getImageCollectionAsString")
	public static Set<String> getImageIds(Set<Image> imageFiles) {
		Set<String> imgs = new HashSet<>();
		imgs = imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
		return imgs;
	}
//
//	 @Named("getImageAsStringForRequset")
//	 public static  Set<Image> getImage( Set<String> imageFiles) {
//			Set<Image> imgs = new HashSet<>();
//			imgs = imageFiles.stream().map(imFile->imFile.
//			 return imgs;
//		}

}
