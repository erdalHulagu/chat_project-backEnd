package com.sohbet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

	private Long id;
	
	private String firstName;

	private String lastName;

	private String email;
	
	private String password;

	private String phone;
	
	private String address;
	
	private String postCode;

}
