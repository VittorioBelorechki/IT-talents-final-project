package com.vtube.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.IDTO;
import com.vtube.dto.LoginDTO;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.dto.UserDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.BadCredentialsException;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.ConflictException;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.InvalidAgeException;
import com.vtube.exceptions.InvalidEmailException;
import com.vtube.exceptions.InvalidNameException;
import com.vtube.exceptions.InvalidPasswordException;
import com.vtube.exceptions.NotLoggedInException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.service.SessionService;
import com.vtube.service.UserService;

@RestController
public class UserController {
	
	private static final String APPLICATION_MAIN_PAGE = "https://vtubeto.postman.co";

	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionService session;
	
	@PostMapping("/signup")
	@ResponseBody
	public UserDTO signUp(@RequestBody SignUpDTO signUpData, HttpServletRequest request)
			throws BadCredentialsException, InvalidNameException, InvalidEmailException,
			InvalidPasswordException, InvalidAgeException, EmailExistsException, UserExistsException{
		UserDTO user = null;
		
		try {
			if(this.session.getUserId(request) != null) {
				throw new BadCredentialsException("Already logged in!");
			}
		} catch (NotLoggedInException e) {
			user = this.userService.signUpUser(signUpData);
			this.session.createSession(request, user.getId());
		}
		
		return user;
	}
	
	@GetMapping("/")
	public void mainPage(HttpServletResponse response) throws IOException {
		response.sendRedirect(APPLICATION_MAIN_PAGE);
	}
	
	@GetMapping("/user/{id}")
	public IDTO getUserById(@PathVariable long id) throws UserNotFoundException {
		UserDTO user = this.userService.getUserDTOById(id);
		return user;
	}
	
	@PostMapping("/login")
	@ResponseBody
	public IDTO login(@RequestBody LoginDTO user, HttpServletRequest request) throws BadCredentialsException{
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		try {
			this.session.getUserId(request);
			message.setMessage("Someone is already logged in");
		} catch (NotLoggedInException e) {
			Long userId = this.userService.login(user);
			this.session.createSession(request, userId);
			message.setMessage("You logged in!");
		}
		return message;
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public IDTO logout(HttpServletRequest request){
		SimpleMessageDTO message = new SimpleMessageDTO();
		
		try {
			this.session.getUserId(request);
			this.session.invalidateSession();
			message.setMessage("You logged out!");
		} catch (NotLoggedInException e) {
			message.setMessage("Noone is logged in!");
		}
		
		return message;
	}
	
	@GetMapping("/videos")
	@ResponseBody
	public List<VideoDTO> getUserVideos(
			@RequestParam(name= "watched", required = false) boolean watched,
			@RequestParam(name= "forLater", required = false) boolean forLater,
			HttpServletRequest request) throws NotLoggedInException{
		Long userId = this.session.getUserId(request);
		
		if(watched == true) {
			List<VideoDTO> watchedVideos = this.userService.getUserWatchedVideos(userId);
			return watchedVideos;
		}
		
		if(forLater == true) {
			List<VideoDTO> videosForLater = this.userService.getUserVideosForLater(userId);
			return videosForLater;
		}
		
		return null;
	}
	
	@GetMapping("/liked/{userId}")
	@ResponseBody
	public List<VideoDTO> getUserLikedVideos(@PathVariable Long userId, HttpServletRequest request) throws UserNotFoundException{
		List<VideoDTO> videos = this.userService.getUserLikedVideos(userId);
		
		return videos;
	}
	
	@PostMapping("/like")
	public IDTO likeVideo (@RequestParam(name= "videoId", required = false) Long videoId, HttpServletRequest request) throws NotLoggedInException, VideoNotFoundException {
		Long userId = this.session.getUserId(request);
		if(videoId == null) {
			throw new VideoNotFoundException("");
		}
		
		IDTO message = this.userService.likeVideo(videoId, userId);
		
		return message;
	}
	
	@DeleteMapping("/like")
	public IDTO removeVideoLike (@RequestParam(name= "videoId", required = false) Long videoId, HttpServletRequest request) throws NotLoggedInException, VideoNotFoundException {
		Long userId = this.session.getUserId(request);
		if(videoId == null) {
			throw new VideoNotFoundException("");
		}
		
		IDTO message = this.userService.removeVideoLike(videoId, userId);
		
		return message;
	}
	
	@PostMapping("/dislike")
	public IDTO dislikeVideo (@RequestParam(name= "videoId", required = false) Long videoId, HttpServletRequest request) throws NotLoggedInException, VideoNotFoundException {
		Long userId = this.session.getUserId(request);
		if(videoId == null) {
			throw new VideoNotFoundException("");
		}
		IDTO message = this.userService.dislikeVideo(videoId, userId);
		
		return message;
	}
	
	@DeleteMapping("/dislike")
	public IDTO removeVideoDislike (@RequestParam(name= "videoId", required = false) Long videoId, HttpServletRequest request) throws NotLoggedInException, VideoNotFoundException {
		Long userId = this.session.getUserId(request);
		if(videoId == null) {
			throw new VideoNotFoundException("");
		}
		IDTO message = this.userService.removeVideoDislike(videoId, userId);
		return message;
	}

	@PostMapping("/subscribe")
	public IDTO subscribeToChannel(@RequestParam("channelId") Long channelId, HttpServletRequest request) throws NotLoggedInException, ChannelNotFoundException, ConflictException {
		Long userId = this.session.getUserId(request);
		
		IDTO message = this.userService.subscribeToChannel(userId, channelId);
		
		return message;
	}

	@PostMapping("/watchLater/{videoId}")
	public IDTO watchVideoLater(@PathVariable Long videoId, HttpServletRequest request) throws NotLoggedInException, VideoNotFoundException {
		Long userId = this.session.getUserId(request);	
		
		SimpleMessageDTO message = this.userService.watchVideoLater(userId, videoId);
		
		return message;
	}
}
