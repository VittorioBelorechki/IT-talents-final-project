package com.vtube.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.ChannelDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.service.ChannelService;

@RestController
public class ChannelController {
	@Autowired
	ChannelService channelService;
	
	@PostMapping("/channel")
	@ResponseBody
	SimpleMessageDTO createChannel(HttpServletRequest request) {
		SimpleMessageDTO message = new SimpleMessageDTO();
		if(request.getSession(false) == null) {
			message.setMessage("You are not logged in!");
		} else {
			HttpSession session = request.getSession(false);
			Long userId = (Long) session.getAttribute("userId");
			this.channelService.createChannel(userId);
			message.setMessage("Channel created!");
		}

		return message;
	}
	
	/**
	 * This method will return or channel specified by id, or user's own channel
	 * 
	 * @param id if exists, must return specific channel by it, otherwise must return user's channel
	 * @param request to validate if user is logged in
	 * @return ChannelDTO object which represents the view of channel interface
	 * @throws ChannelNotFoundException if user is logged in, but parameter for channel is invalid
	 * @throws UserDoNotHaveChannelException if there is not parameter in request, and user do not have his own channel
	 * 
	 */
	@GetMapping("/channel")
	ChannelDTO getChannelbyID(@RequestParam(name="id", required= false) Long id, HttpServletRequest request) throws ChannelNotFoundException, UserDoNotHaveChannelException {
		ChannelDTO channel = null;
		
		//if no user is logged in
		if(request.getSession(false) == null) {
			if(id == null) {
				throw new ChannelNotFoundException("Not logged in");
			}
			channel = this.channelService.getChannelById(id);
		} else {
			
			//if user is logged in and there is no parameter in request
			if(id != null) {
				channel = this.channelService.getChannelById(id);
			} else {
				HttpSession session = request.getSession(false);
				channel = this.channelService.getChannelDTOByUserId((Long) session.getAttribute("userId"));
			}

		}
		
		return channel;
	}
}
