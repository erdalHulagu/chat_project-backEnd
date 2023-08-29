package com.sohbet.request;


import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
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
	
	@Size(max=50)
	@NotBlank(message="Please provide your First name")
	private String firstName;
	
	@Size(max=50)
	@NotBlank(message="Please provide your Last name")
	private String lastName;
	
	@Size(min=5  , max=80)
	@Email(message="Please provide your email")
	private String email;
	
	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",  
            message = "Please provide valid phone number")
	@Size(max=14)
	@NotBlank(message="Please provide your phone number")
	private String phoneNumber;
	
	@Size(max=100)
	@NotBlank(message="Please provide your address")
	private String address;
	
	@NotBlank(message="Please provide your update time")
	private LocalDateTime createAt;

	@NotBlank(message="Please provide your update time")
	private LocalDateTime updateAt;
	
	@Column
	private Set<String> image; 

		

}
