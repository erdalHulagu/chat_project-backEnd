package com.sohbet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
	
	private Long userId;
	 
	private Long chatId;
	
	private String content;
	
	
	
	
	

}
