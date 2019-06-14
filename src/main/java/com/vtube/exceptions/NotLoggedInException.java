package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Not logged in!")
public class NotLoggedInException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2316763413216382138L;

	public NotLoggedInException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotLoggedInException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
