package com.sohbet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "t_user")

public class User {

	
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@NotNull
@Column(name="name", nullable = false, length = 100)
private String firstName;

@NotNull
@Column(name="surname",nullable = false, length = 100)
private String lastName;


@Email(message = "Please provide valid email")
@Size(min = 10, max = 80)
@Column(length = 80, nullable = false, unique=true)
private String email;


@Size(min = 10, max = 80)
@Column(length = 80, nullable = false, unique=true)
private String password;

@Size(max= 100)
@NotBlank(message = "Please provide your address")
@Column(length = 80, nullable = false, unique=true)
private String address;
//
//@Pattern(regexp = "\\\\d{3}-\\\\d{3}-\\\\d{4}",	// 999-999-9999
//message = "Please provide valid phone number" ) 
//@Column(nullable = false)
//private String phone;

//@UpdateTimestamp
//@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//private LocalDateTime updateAt;
//
//
@Column(name = "create_at", updatable = false, nullable = true)
@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime createAt;


@OneToMany(fetch = FetchType.LAZY)
//@JoinColumn(name="user_Id")
//@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // CascadeType.ALL: Eşleşen resim verisini silerken kullanıcıyı da siler
@JoinColumn(name = "user_id")
private Set<Image> profileImage;



@ManyToMany   // hibernate defaultta LAZY
@JoinTable( name="t_user_role",
						 joinColumns = @JoinColumn(name="user_id"),
						 inverseJoinColumns = @JoinColumn(name="role_id"))
private  Set<Role> roles = new HashSet<>();

//
//@OneToMany(fetch = FetchType.LAZY)
////@JoinColumn(name="user_Id")
////@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // CascadeType.ALL: Eşleşen resim verisini silerken kullanıcıyı da siler
//@JoinColumn(name = "user_id")
//private Set<Image> myImages;

//su_an = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")  su method su anin tarihini alir yani current time

}

