package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="File with such name already exists!")
public class FileExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3437685246133571700L;

	public FileExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FileExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FileExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
