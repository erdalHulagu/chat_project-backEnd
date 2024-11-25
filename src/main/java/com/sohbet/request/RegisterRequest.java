package com.sohbet.request;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.sohbet.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@Size(max = 50)
	@NotBlank(message = "Please provide yout First Name")
	private String firstName;

	@Size(max = 50)
	@NotBlank(message = "Please provide yout Last Name")
	private String lastName;

	@Size(min = 5, max = 80)
	@Email(message = "Please provide valid e-mail")
	private String email;

	@Size(min = 4, max = 20, message = "Please provide correct size of password")
	@NotBlank(message = "Please provide your password")
	private String password;

	private String phone;

	@Size(max = 100)
	@NotBlank(message = "Please provide your address")
	private String address;

	private LocalDateTime updateAt;

	@NotBlank(message = "Please provide post code")
	private String postCode;

	private boolean builtIn;

	private Set<String> roles = new HashSet<>();

	public void setRoles(Set<Role> roles) {

		Set<String> roleStr = new HashSet<>();

		roles.forEach(r -> {
			roleStr.add(r.getType().getName()); // Administrator veya Customer gözükecek

		});

		this.roles = roleStr;
	}

}