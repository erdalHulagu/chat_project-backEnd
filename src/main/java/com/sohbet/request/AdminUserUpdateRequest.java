package com.sohbet.request;

import java.time.LocalDateTime;
import java.util.Set;

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
public class AdminUserUpdateRequest {
	@Size(max=50)
	@NotBlank(message="Please provide your First name")
	private String firstName;
	
	@Size(max=50)
	@NotBlank(message="Please provide your Last name")
	private String lastName;
	
	@Size(min=5  , max=80)
	@Email(message="Please provide your email")
	private String email;
	
	@Size(min=4, max=20, message="Please provide Correct Size of Password")
	@NotBlank(message="Please provide your password")
	private String password;
//	
//	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",  
//            message = "Please provide valid phone number")
//	@Size(max=14)
//	@NotBlank(message="Please provide your phone number")
//	private String phoneNumber;
	
	@Size(max=100)
	@NotBlank(message="Please provide your address")
	private String address;
	

	
	private LocalDateTime createAt;

	
	private LocalDateTime updateAt;
	
	private Set<String> image ;
	
	private Boolean builtIn ;
	
	private Set<String> role ;


}
