package com.vtube.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vtube.dto.BigVideoDTO;
import com.vtube.dto.CreatedVideoDTO;
import com.vtube.dto.IDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.EmptySearchException;
import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.NotLoggedInException;
import com.vtube.exceptions.UnauthorizedException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.Video;
import com.vtube.service.ChannelService;
import com.vtube.service.SessionService;
import com.vtube.service.VideoService;

@RestController
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	ChannelService channelService;
	
	@Autowired
	SessionService session;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/videos")
	@ResponseBody
	public CreatedVideoDTO uploadVideo(
			@RequestParam("file") MultipartFile file, 
			@RequestParam("thumbnail") MultipartFile thumbnail, 
			@RequestParam(name= "title", required = false) 
			String title, @RequestParam(name="description", required = false) 
			String description, 
			HttpServletRequest request
			) throws NotLoggedInException, UserDoNotHaveChannelException, VideoNotFoundException, FileExistsException, UnsupportedFileFormatException {
		Long userId = this.session.getUserId(request);
		
		Channel channel = null;
		try {
			channel = this.channelService.getChannelByUserId(userId);
		} catch (UserDoNotHaveChannelException e) {
			throw new UserDoNotHaveChannelException("You do not have channel and cannot upload video!", e);
		}
		
		CreatedVideoDTO video = this.videoService.uploadVideoData(file, thumbnail, title, description, userId, channel);
		
		return video;
	}
	
	@GetMapping("/watch")
	@ResponseBody
	public BigVideoDTO watchVideoByID(@RequestParam("videoId") Long id, HttpServletRequest request) throws VideoNotFoundException {
		Long userId = null;
		try {
			userId = this.session.getUserId(request);
		} catch (NotLoggedInException e) {
			BigVideoDTO video = this.videoService.getBigVideoDTOById(id);
			return video;
		}
		BigVideoDTO video = this.videoService.getBigVideoDTOById(id, userId);
		return video;
	}
	
	
	@GetMapping("/videosSearch")
	@ResponseBody
	public List<IDTO> searchVideos(@RequestParam("search") String search) {
		
		if (search == null || search.isEmpty()) {
			try {
				throw new EmptySearchException("Your search must have at least one letter!");
			} catch (EmptySearchException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("Your search must have at least one letter!");
				List<IDTO> messages = new LinkedList<IDTO>();
				messages.add(message);
				return messages;
			}
		}
		
		List<Video> videos = this.videoService.findAllBySearchString(search);
		if (videos == null || videos.isEmpty()) {
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No videos match your search!");
				List<IDTO> messages = new LinkedList<IDTO>();
				messages.add(message);
				return messages;
		}
		List<IDTO> videoDTOs = new LinkedList<IDTO>();
		
		for (Video video : videos) {
			VideoDTO videoDTO = new VideoDTO();
			this.mapper.map(video, videoDTO);
			videoDTO.setChannelName(video.getOwner().getName());
			videoDTO.setChannelId(video.getOwner().getId());
			videoDTOs.add(videoDTO);
		}
		return videoDTOs;
	}
	
	
	@DeleteMapping("/videos")
	@ResponseBody
	public IDTO deleteVideo(@RequestParam("videoId") Long videoId, HttpServletRequest request) throws UnauthorizedException, NotLoggedInException, VideoNotFoundException {
		
		if (!this.videoService.findById(videoId)) {
			throw new VideoNotFoundException("No such video!");
		}
		
		Long sessionUserId = this.session.getUserId(request);
		
		Long videoUserId = this.videoService.getVideoById(videoId).getOwner().getOwner().getId();
		if (sessionUserId != videoUserId) {
			throw new UnauthorizedException("You are allowed to delete your videos only!");
		}
		
		this.videoService.deleteVideo(videoId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your video was deleted!");
		return message;
	}
	
}
