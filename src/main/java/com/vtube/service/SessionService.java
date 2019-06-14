package com.vtube.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.exceptions.NotLoggedInException;

import lombok.Data;

/**
 * This is a class to manage session related requests
 */
@Data
@Service
public class SessionService {
	
	private static final String USER_ID = "userId";
	
	@Autowired
	private HttpSession session;
	
	public void createSession(HttpServletRequest request, Long id) {
		this.session = request.getSession();
		this.session.setAttribute(USER_ID, id);
	}
	
	public Long getUserId(HttpServletRequest request) throws NotLoggedInException{
		this.session = request.getSession(false);
		if(this.session == null) {
			throw new NotLoggedInException();
		}
		
		return (Long) session.getAttribute(USER_ID);
	}
	
	public void invalidateSession () {
		this.session.invalidate();
	}
}
