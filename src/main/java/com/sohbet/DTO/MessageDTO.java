package com.sohbet.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
	
	private Long id;
	
	private String content;
	
	private LocalDateTime createAt;
	
	private Long userId;
	
	private Long chatId;
	
	

}
