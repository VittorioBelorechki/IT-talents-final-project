package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Search keyword must have at least one letter!")
public class EmptySearchException extends Exception {

	public EmptySearchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmptySearchException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public EmptySearchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public EmptySearchException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public EmptySearchException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8511271934508486864L;

}
