package com.sohbet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String id;
	// constructor 
	public ResourceNotFoundException(String message) {
		super(message );
	}	
		public ResourceNotFoundException(String message, String id) {
			super(message );
		    this.id=id;
	}

	
	

}
