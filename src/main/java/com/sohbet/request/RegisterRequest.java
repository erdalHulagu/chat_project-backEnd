package com.sohbet.request;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	
	@Size(max=50)
	@NotBlank(message="Please provide yout First Name")
	private String firstName;
	
	@Size(max=50)
	@NotBlank(message="Please provide yout Last Name")
	private String lastName;
	
	@Size(min=5, max=80)
	@Email(message="Please provide valid e-mail")
	private String email;
	
	@Size(min=4, max=20, message="Please provide Correct Size of Password")
	@NotBlank(message="Please provide your password")
	private String password;
	
	 @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
	            message = "Please provide valid phone number")
	@Size(min=14, max=14)
    @NotBlank(message = "Please provide your phone number")
	private String phoneNumber;
	
    @Size(max= 100)
    @NotBlank(message = "Please provide your address")
	private String address;

	
    @CreationTimestamp
    @Column(name = "creatAt", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updateAt;

    private Set<String> image;
}
