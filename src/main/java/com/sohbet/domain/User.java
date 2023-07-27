package com.sohbet.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	private Set<Image> image;
	
	@Column(nullable = false)
	private Boolean builtIn = false; // silinmesini ve değiştirilmesi istenmeyen obje..
	
	
	@OneToMany   // hibernate defaultta LAZY
	@JoinTable( name="userRole",joinColumns = @JoinColumn(name="userId"),inverseJoinColumns = @JoinColumn(name="roleId"))
	private  Set<Role> role = new HashSet<>();
}



