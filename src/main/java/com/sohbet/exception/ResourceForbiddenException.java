package com.sohbet.exception;

public class ResourceForbiddenException extends RuntimeException {
	
	 private static final long serialVersionUID = 1L;

	public ResourceForbiddenException() {
	        super();
	    }

	    public ResourceForbiddenException(String message) {
	        super(message);
	    }

	    public ResourceForbiddenException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
}
