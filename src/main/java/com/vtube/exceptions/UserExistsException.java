package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="User with that user name already exists")
public class UserExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8975452256124874824L;

	public UserExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }	
}
