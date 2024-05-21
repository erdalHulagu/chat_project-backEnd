package com.sohbet.request;
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

	@Size(max = 50)
	@NotBlank(message = "Please provide your First name")
	private String firstName;

	@Size(max = 50)
	@NotBlank(message = "Please provide your Last name")
	private String lastName;

	@Size(min = 5, max = 80)
	@Email(message = "Please provide your email")
	private String email;

	@Size(min = 4, max = 80)
	private String password;

	@Pattern(regexp = "^(\\d{4} \\d{3} \\d{2} \\d{2})$", // 9999 999 99 99
			message = "Please provide valid phone number")
	@Size(max = 14)
	@NotBlank(message = "Please provide your phone number")
	private String phone;

	@Size(max = 100)
	@NotBlank(message = "Please provide your address")
	private String address;

	

}
