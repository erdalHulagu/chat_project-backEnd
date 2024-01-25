package com.sohbet.domain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.sohbet.request.AdminUserUpdateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
@Entity
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String chatName;
		
	@Column(name = "chat_id")
	@ManyToMany 
    private Set<Image> chatImage=new HashSet<>();
   
	@Column(name = "admin")
	@OneToMany
    private Set<User> admin=new HashSet<>();
	
	@Column(name = "is_group")
	private Boolean isGroup;
	
	@JoinColumn(name = "createdBy")
	@ManyToOne
	private User createdBy;
	
	@ManyToMany()
	private Set<User> users=new HashSet<>();
	
	@OneToMany(orphanRemoval = true)
	private List<Message>messages=new ArrayList<>();
		
	}
	
	
	
	

