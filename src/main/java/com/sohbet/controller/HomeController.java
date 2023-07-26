package com.sohbet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sohbet.DTOresponse.ChatResponse;
import com.sohbet.DTOresponse.ResponseMessage;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public ResponseEntity<ChatResponse> home() {
		
		ChatResponse chatResponce=new ChatResponse(ResponseMessage.HOME_OPEN_MESSAGE,true);
		
		return ResponseEntity.ok(chatResponce);
		
	}

}
