package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="You can't add subcomment on a subcomment!")
public class IllegalSubcommentException extends Exception {

	public IllegalSubcommentException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IllegalSubcommentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IllegalSubcommentException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IllegalSubcommentException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalSubcommentException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2474257082945746987L;

}
