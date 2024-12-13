package com.sohbet.request;




import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

	
	private String firstName;

	private String lastName;

	private String email;
	
	private String password;

	private String phone;
	
	private String address;
	
	private String postCode;

	public String profileImage;



}
