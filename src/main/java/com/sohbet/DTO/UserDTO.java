package com.sohbet.DTO;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.sohbet.domain.Role;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	
	
	private String firstName;
	
	
	private String lastName;
	
	
	private String email;
	
	
	private String password;
	
	
	private String phoneNumber;
	
	
	private String address;
	
	
	private Set<String> image;
	

	private OffsetDateTime createAt;


	private OffsetDateTime updateAt;
	
	private Boolean builtIn ; // silinmesini ve değiştirilmesi istenmeyen obje..
	
	
	private  Set<String> roles ;
	
	public void setRoles(Set<Role> roles) {
		
		Set<String> roleStr = new HashSet<>();
		
		roles.forEach( r-> {
			roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek
			
		}); 
		
		this.roles = roleStr;
	}
	
	

}
