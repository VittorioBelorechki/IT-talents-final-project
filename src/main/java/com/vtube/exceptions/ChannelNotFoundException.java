package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such channel!")
public class ChannelNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6590232120355273941L;

	public ChannelNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChannelNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ChannelNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ChannelNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ChannelNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
