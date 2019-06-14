package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid input for age")
public class InvalidAgeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4795814490785288578L;

	public InvalidAgeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidAgeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidAgeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidAgeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidAgeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
