package com.sohbet.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
	
	private Long id;
	
	private String chatName;
	
	
	private Set<String> chatImage=new HashSet<>();
	
	
	private Boolean isGroup;
	
	
	private User createdBy;
	

	private Set<User> users=new HashSet<>();
	
	
	private List<Message>messages=new ArrayList<>();
		
}