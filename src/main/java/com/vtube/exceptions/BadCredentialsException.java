package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Wrong input data!")
public class BadCredentialsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2917171167989614973L;

	public BadCredentialsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
