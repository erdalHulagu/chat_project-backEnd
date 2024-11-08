package com.sohbet.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.Named;

import com.sohbet.domain.Message;
import com.sohbet.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {

	private String chatName;

	
	private Boolean isGroup;

	
	private Long createdBy;

	
	private Set<Long> admins = new HashSet<>();

	
	private Set<Long> users = new HashSet<>();

	
	private List<Long> messages = new ArrayList<>();
	
	
	private Set<String> chatImage = new HashSet<>();




}