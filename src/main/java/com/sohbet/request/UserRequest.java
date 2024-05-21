package com.sohbet.request;

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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


public class UserRequest {

@NotBlank(message="Please provide your First name")
private String firstName;


@NotBlank(message="Please provide your Last name")
private String lastName;

@Email(message = "Please provide valid email")
@Size(min = 10, max = 80)
private String email;


@NotBlank(message="Please provide your password")
private String password;



@NotBlank(message="Please provide your password")
private Set<String>  myImages;

@Pattern(regexp ="^(\\d{4} \\d{3} \\d{2} \\d{2})$",	// 9999 999 99 99
message = "Please provide valid phone number" ) 
@Column(nullable = false)
private String phone;



@NotBlank(message="Please provide your address")
private String address;


private byte[] profileImage; 






}
