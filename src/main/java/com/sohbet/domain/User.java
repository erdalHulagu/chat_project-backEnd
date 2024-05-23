package com.sohbet.domain;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;
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
@Column(length = 80, nullable = false, unique=true, updatable = false)
private String email;


@Size(min = 10, max = 80)
@Column(length = 80, nullable = false, unique=true)
private String password;

@Size(max= 100)
@NotBlank(message = "Please provide your address")
@Column(length = 80, nullable = false, unique=true)
private String address;

@Pattern(regexp ="^(\\d{4} \\d{3} \\d{2} \\d{2})$",	// 9999 999 99 99
message = "Please provide valid phone number" ) 
@Column(nullable = true) 
private String phone;


@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
@Column(name = "update_at",length = 30, nullable = false, updatable = true)
private LocalDateTime updateAt;


@Column(name = "create_at", length = 30, updatable = false, nullable = true)
@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime createAt;

@ManyToMany   
@JoinTable( name="t_user_role",
						 joinColumns = @JoinColumn(name="user_id"),
						 inverseJoinColumns = @JoinColumn(name="role_id"))
private  Set<Role> roles = new HashSet<>();

//
//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // CascadeType.ALL: Eşleşen resim verisini silerken kullanıcıyı da siler
//@JoinColumn(name = "user_image_ids",nullable = true)
//private Set<Image> myImages;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "profile_imageId", referencedColumnName = "id",nullable = true)
private Image profileImage;

//su_an = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")  su method su anin tarihini alir yani current time

@OneToMany(orphanRemoval = true, mappedBy = "user")
private List<Message>messages=new ArrayList<>();
	
@OneToMany(mappedBy = "createdBy")
private List<Chat> chat;

@ManyToMany(mappedBy = "users")
private Set<Chat> chats = new HashSet<>();

@ManyToMany   // hibernate defaultta LAZY
@JoinTable( name="t_user_friend",
						 joinColumns = @JoinColumn(name="user_id"),
						 inverseJoinColumns = @JoinColumn(name="friend_id"))
private List<Friend>friends= new ArrayList<>();







public void setCreateTime(LocalDateTime createAt) {
	
createAt=LocalDateTime.now();

this.createAt=createAt;
this.updateAt=createAt;

}
}

