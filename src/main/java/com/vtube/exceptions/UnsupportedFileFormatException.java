package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Unsupported file format!")
public class UnsupportedFileFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1725848399631008183L;

	public UnsupportedFileFormatException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnsupportedFileFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedFileFormatException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedFileFormatException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedFileFormatException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
