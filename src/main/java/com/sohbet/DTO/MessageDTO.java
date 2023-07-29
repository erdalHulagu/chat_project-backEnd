package com.sohbet.DTO;

import java.time.LocalDateTime;

import com.sohbet.domain.Chat;
import com.sohbet.domain.Message;
import com.sohbet.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
	
	
	private Long id;
	
	private String content;
	
	private LocalDateTime createTime;
	

	private User user;
	
	
	private Chat chat;

}
