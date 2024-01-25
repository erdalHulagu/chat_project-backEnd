package com.sohbet.DTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

//private  LocalDateTime updateAt;
//
private String phone;

private LocalDateTime createAt;

private String address;


private Set<String> profileImage;

private  Set<String> roles ;

public void setRoles(Set<Role> roles) {
	
	Set<String> roleStr = new HashSet<>();
	
	roles.forEach( r-> {
		roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek
		
	}); 
	
	this.roles = roleStr;
}


	
}



