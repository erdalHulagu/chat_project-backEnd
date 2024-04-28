package com.sohbet.request;

import java.time.LocalDateTime;
import java.util.Set;

import com.sohbet.domain.Image;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

private Long id;

@NotNull
@NotBlank(message="Please provide your First name")
private String firstName;

@NotNull
@NotBlank(message="Please provide your Last name")
private String lastName;

@Email(message = "Please provide valid email")
@Size(min = 10, max = 80)
private String email;

@NotNull
@NotBlank(message="Please provide your password")
private String password;

//@NotNull
//@NotBlank(message="Please provide updateTime")
//private  LocalDateTime updateAt;
////
//@Pattern(regexp = "\\\\d{3}-\\\\d{3}-\\\\d{4}",	// 999-999-9999
//message = "Please provide valid phone number" ) 
//@Column(nullable = false)
//private String phone;
//
//
@NotNull
@NotBlank(message="Please provide createTime")
private LocalDateTime createAt;

@NotNull
@NotBlank(message="Please provide your address")
private String address;

private Image image; 



//@Pattern(regexp = "\\\\d{3}-\\\\d{3}-\\\\d{4}",	// 999-999-9999
//message = "Please provide valid phone number" ) 
//@Column(nullable = false)
//private String phone;








}
