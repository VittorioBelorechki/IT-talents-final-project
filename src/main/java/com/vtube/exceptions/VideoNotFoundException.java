package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Video not found!")
public class VideoNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7743107098512776034L;

	public VideoNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VideoNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public VideoNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public VideoNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public VideoNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }	

}
