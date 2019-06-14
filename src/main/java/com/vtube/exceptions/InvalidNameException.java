package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid input for name")
public class InvalidNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 691773414457800246L;
	
	public InvalidNameException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidNameException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public InvalidNameException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public InvalidNameException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidNameException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }


}
