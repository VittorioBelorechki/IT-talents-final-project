package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid input for email")
public class InvalidEmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7835874571425134773L;

	public InvalidEmailException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
	
}
