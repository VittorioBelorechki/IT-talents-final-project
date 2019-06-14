package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="You are allowed to manipulate your videos and comments only!")
public class UnauthorizedException extends Exception {

	public UnauthorizedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155304085972348082L;
	
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }	

}
