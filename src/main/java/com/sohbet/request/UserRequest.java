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


private String phone;


@NotBlank(message="Please provide your address")
private String address;


private Boolean postCode;

private String profileImage; 

@NotBlank(message="Please provide your password")
private Set<String>  myImages;




}
