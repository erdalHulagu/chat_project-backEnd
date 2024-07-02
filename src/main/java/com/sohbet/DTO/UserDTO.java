package com.sohbet.DTO;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Named;

import com.sohbet.domain.Image;
import com.sohbet.domain.Role;
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

private Boolean builtIn;

private Set<String> profileImage;

private Set<String>  myImages;

private  Set<String> roles ;


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
public void setProfileImage(Set<Image> images) {
	Set<String> imageStr = new HashSet<>();
	for (Image image : images) {
		imageStr.add(String.valueOf(image.getId())); // Assuming Image class has a getUrl method
	}
	this.profileImage = imageStr;
}



}



