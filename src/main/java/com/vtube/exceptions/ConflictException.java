package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason= "Conflict occured!")
public class ConflictException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8585076485387741849L;

	public ConflictException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ConflictException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ConflictException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ConflictException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
