package com.sohbet.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name="user")
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String firstName;
	
	@Column(length = 50, nullable = false)
	private String lastName;
	
	@Email
	@Column(length = 80, nullable = false, unique = true)
	private String email;
	
	@Column(length = 120, nullable = false)
	private String password;
	
	@Pattern(message = "Please provide valid phone number",regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")//(541) 317-8828
	@Size(min=14, max=14)
    @NotBlank(message = "Please provide your phone number")
	@Column(length = 14, nullable = false)
	private String phoneNumber;
	
	@CreationTimestamp
	@Column(name = "createAt", nullable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createAt;

	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "updateAt")
	private LocalDateTime updateAt;
	
	@Column(length =100, nullable = false)
	private String address;
	
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name="userId")
	private Set<Image> image=new HashSet<>();
	
	@Column(nullable = false)
	private Boolean builtIn = false; // silinmesini ve değiştirilmesi istenmeyen obje..
	
	
	@OneToMany   // hibernate defaultta LAZY
	@JoinTable( name="userRole",joinColumns = @JoinColumn(name="userId"),inverseJoinColumns = @JoinColumn(name="roleId"))
	private  Set<Role> role = new HashSet<>();
}



