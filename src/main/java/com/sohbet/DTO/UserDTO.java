package com.sohbet.DTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Named;

import com.sohbet.domain.Chat;
import com.sohbet.domain.Image;
import com.sohbet.domain.Message;
import com.sohbet.domain.Role;
import com.sohbet.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
	
private Long id;

private String firstName;

private String lastName;

private String email;

private String password;

private String phone;

private String address;

private String postCode;

private LocalDateTime updateAt;

private LocalDateTime createAt;

private Boolean builtIn;

private String profileImage;

private List<MessageDTO> messages;

private Set<String>  myImages;

private  Set<String> roles ;

private Set<Long> chatList;

private Set<Long> chats;

private Set<Long> chatAdmins;



//--------------------
public void setRoles(Set<Role> roles) {
	
	Set<String> roleStr = new HashSet<>();
	
	roles.forEach( r-> {
		roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek
		
	}); 
	
	this.roles = roleStr;
}


//---------------------
public void setMyImages(Set<Image> images) {
    Set<String> imgs = new HashSet<>();
	imgs = images.stream().map(img -> img.getId().toString()).collect(Collectors.toSet());
	this.myImages=imgs;
}


//----------------------
public void setProfileImage(Image image) {
	    if (image != null) {
	        this.profileImage = image.getId().toString();
	    } else {
	        this.profileImage = null;
	    }
	}



}





