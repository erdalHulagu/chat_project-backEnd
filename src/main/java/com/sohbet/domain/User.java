package com.sohbet.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Entity
@Table(name = "t_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false, length = 100)
	private String firstName;

	@NotNull
	@Column(name = "surname", nullable = false, length = 100)
	private String lastName;

	@NotNull
	@Email(message = "Please provide a valid email")
	@Size(min = 4, max = 80)
	@Column(length = 80, nullable = false, unique = true, updatable = false)
	private String email;

	@NotNull
	@Size(min = 4, max = 80)
	@Column(length = 80, nullable = false, unique = true)
	private String password;

//	@Pattern(regexp = "^(\\d{4} \\d{3} \\d{2} \\d{2})$", message = "Please provide a valid phone number")
	@Column(nullable = true)
	private String phone;

	@Size(max = 100)
	@NotNull(message = "Please provide your address")
	@Column(length = 80, nullable = false, unique = true)
	private String address;

	@Size(max = 20)
	@NotNull(message = "Please provide a post code")
	@Column(length = 10, nullable = true)
	private String postCode;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "update_at", length = 30, updatable = true)
	@UpdateTimestamp
	private LocalDateTime updateAt;

	@Column(name = "create_at", length = 30, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createAt;

	private Boolean builtIn;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
	    name = "user_images", // Ara tablo adı
	    joinColumns = @JoinColumn(name = "user_id"), // User tablosundan ilişki
	    inverseJoinColumns = @JoinColumn(name = "image_id") // Image tablosundan ilişki
	    
	)
	private Set<Image> myImages = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_image_id")
	@JsonIgnore
	private Image profileImage;
	

	@OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
	private List<Message> messages = new ArrayList<>();

	@OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
	private List<Chat> chatList;

	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<Chat> chats = new ArrayList<>();

	@ManyToMany(mappedBy = "admins", fetch = FetchType.LAZY)
	private Set<Chat> chatAdmins = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "user_friends",
	           joinColumns = @JoinColumn(name = "user_id"),
	           inverseJoinColumns = @JoinColumn(name = "friend_id"))
	private List<User> friends = new ArrayList<>();

//    
//    @Override
//    public boolean equals(Object objct) {
//        if (this == objct) return true;
//        if (objct == null || getClass() != objct.getClass()) return false;
//        User user = (User) objct;
//        return Objects.equals(id, user.id);  // User'ları id'ye göre kıyasla
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public String toString() {
//        return "User [id=" + id + ", firstName=" + firstName + "]";
//    }
}