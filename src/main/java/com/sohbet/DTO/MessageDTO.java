package com.sohbet.DTO;

import java.time.LocalDateTime;

import com.sohbet.domain.Chat;
import com.sohbet.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
	
	
	private String content;
	
	private LocalDateTime createTime;
	

	private User user;
	
	
	private Chat chat;

}
