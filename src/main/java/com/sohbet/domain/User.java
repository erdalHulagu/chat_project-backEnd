package com.sohbet.domain;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
	
	@Column(length = 80, nullable = false, unique = true)
	private String email;
	
	@Column(length = 120, nullable = false)
	private String password;
	
	@Column(length = 14, nullable = false)
	private String phoneNumber;
	
	@CreationTimestamp
	@Column(name = "create_at", nullable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private OffsetDateTime createAt;

	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@Column(name = "update_at")
	private OffsetDateTime updateAt;
	
	@Column(length =100, nullable = false)
	private String address;
	
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name="userId")
	private Set<Image> image;
	
	@Column(nullable = false)
	private Boolean builtIn = false; // silinmesini ve değiştirilmesi istenmeyen obje..
	
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)   // hibernate defaultta LAZY
	@JoinTable( name="userRole",
							 joinColumns = @JoinColumn(name="userId"),
							 inverseJoinColumns = @JoinColumn(name="roleId"))
	private  Set<Role> role = new HashSet<>();
}



