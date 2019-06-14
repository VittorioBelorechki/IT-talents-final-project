package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="This user do not have channel")
public class UserDoNotHaveChannelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2630556694173436033L;

	public UserDoNotHaveChannelException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDoNotHaveChannelException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserDoNotHaveChannelException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserDoNotHaveChannelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserDoNotHaveChannelException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
