package com.vtube.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.CommentDTO;
import com.vtube.dto.ContentDTO;
import com.vtube.dto.IDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.exceptions.IllegalSubcommentException;
import com.vtube.exceptions.NoSuchCommentException;
import com.vtube.exceptions.NotLoggedInException;
import com.vtube.exceptions.UnauthorizedException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Comment;
import com.vtube.service.CommentService;
import com.vtube.service.UserService;
import com.vtube.service.VideoService;


@RestController
public class CommentController {

	private CommentService commentService;
	private VideoService videoService;
	private ModelMapper mapper;
	private UserService userService;

	@Autowired
	public CommentController(CommentService commentService, VideoService videoService, ModelMapper mapper, UserService userService) {
		this.commentService = commentService;
		this.videoService = videoService;
		this.mapper = mapper;
		this.userService = userService;
	}

	@GetMapping("/comments")
	@ResponseBody
	public List<IDTO> getCommentsByVideo(@RequestParam("videoId") Long videoId) {
		
		if (!this.videoService.findById(videoId) ) {
			try {
				throw new VideoNotFoundException("No such video!");
			} catch (VideoNotFoundException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such video!");
				List<IDTO> messages = new LinkedList<IDTO>();
				messages.add(message);
				return messages;
			}
		}
		
		List<Comment> comments = this.commentService.findAllByVideoId(videoId);
		if (comments == null || comments.isEmpty()) {
			SimpleMessageDTO message = new SimpleMessageDTO();
			message.setMessage("There are no comments on this video!");
			List<IDTO> messages = new LinkedList<IDTO>();
			messages.add(message);
			return messages;
		}
		List<IDTO> commentDTOs = new LinkedList<IDTO>();
		
		for (Comment comment : comments) {
			CommentDTO commentDTO = new CommentDTO();
			this.mapper.map(comment, commentDTO);
			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
			List<Comment> subComments = this.commentService.findAllByCommentId( ((long)comment.getId()) );
			for (Comment c : subComments) {
				CommentDTO subCommentDTO = new CommentDTO();
				this.mapper.map(c, subCommentDTO);
				subCommentDTO.setUserNickName(userService.findById(c.getAuthor().getId()).get().getNickName());
				subCommentDTO.add(subCommentDTO);
			}
			commentDTOs.add(commentDTO);
		}
		return commentDTOs;
	}
	
	
	@GetMapping("/commentReplies")
	@ResponseBody
	public List<IDTO> getCommentsBySupercomment(@RequestParam("commentId") Long commentId) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				List<IDTO> messages = new LinkedList<IDTO>();
				messages.add(message);
				return messages;
			}
		}
		
		List<Comment> comments = this.commentService.findAllByCommentId(commentId);
		if (comments == null || comments.isEmpty()) {
			SimpleMessageDTO message = new SimpleMessageDTO();
			message.setMessage("There are no subcomments on this comment!");
			List<IDTO> messages = new LinkedList<IDTO>();
			messages.add(message);
			return messages;
		}
		List<IDTO> commentDTOs = new LinkedList<IDTO>();
		
		for (Comment comment : comments) {
			CommentDTO commentDTO = new CommentDTO();
			this.mapper.map(comment, commentDTO);
			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
			commentDTO.setSuperCommentId(comment.getSuperComment().getId());
			commentDTOs.add(commentDTO);
		}
		return commentDTOs;
	}
	
	
	@PostMapping("/comments")
	@ResponseBody
	public IDTO addComment(@RequestParam("videoId") Long videoId,
			@RequestBody ContentDTO contentDTO, HttpServletRequest request) {
		
		if (!this.videoService.findById(videoId)) {
			try {
				throw new VideoNotFoundException("No such video!");
			} catch (VideoNotFoundException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such video!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		Long userId = (Long) session.getAttribute("userId");
		this.commentService.addComment(contentDTO, userId, videoId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your comment was added!");
		return message;
	}
	
	
	@PostMapping("/subcomments")
	@ResponseBody
	public IDTO addSubComment(@RequestParam("commentId") Long commentId,
			@RequestBody ContentDTO contentDTO, HttpServletRequest request) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				return message;
			}
		}
		
		if (this.commentService.getCommentById(commentId).getSuperComment() != null) {
			try {
			throw new IllegalSubcommentException("You can't add subcomment on a subcomment!");
			} catch (IllegalSubcommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You can't add subcomment on a subcomment!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		Long userId = (Long) session.getAttribute("userId");
		this.commentService.addSubComment(contentDTO, userId, commentId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your comment was added!");
		return message;
		
	}
	
	
	@PutMapping("/comments")
	@ResponseBody
	public IDTO editComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
	
		if (!this.commentService.findById(((long)commentDTO.getId())) ) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				return message;
			}
		}
		
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		long commentOwnerId = this.commentService.getCommentById(commentDTO.getId()).getAuthor().getId();
		long sessionUserId = (long)session.getAttribute("userId");
		
		if (commentOwnerId != sessionUserId) {
			try {
			throw new UnauthorizedException("You are allowed to edit only comments posted by you!");
			} catch (UnauthorizedException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are allowed to edit only comments posted by you!");
				return message;
			}
		}
	
		this.commentService.editComment(commentDTO);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your comment was edited!");
		return message;
	}
	
	
	@DeleteMapping("/comments")
	@ResponseBody
	public IDTO deleteComment(@RequestParam("commentId") Long commentId, HttpServletRequest request) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		long commentOwnerId = this.commentService.getCommentById(commentId).getAuthor().getId();
		long sessionUserId = (long)session.getAttribute("userId");
		
		if (commentOwnerId != sessionUserId) {
			try {
			throw new UnauthorizedException("You are allowed to delete only comments posted by you!");
			} catch (UnauthorizedException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are allowed to delete only comments posted by you!");
				return message;
			}
		}
		
		this.commentService.deleteComment(commentId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your comment was deleted!");
		return message;
	}
	
	
	@PutMapping("/commentsLike")
	@ResponseBody
	public IDTO likeComment(@RequestParam("commentId") Long commentId, HttpServletRequest request) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		this.commentService.likeComment(commentId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your liked the comment!");
		return message;	
	}
	
	
	@PutMapping("/commentsDislike")
	@ResponseBody
	public IDTO dislikeComment(@RequestParam("commentId") Long commentId, HttpServletRequest request) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		this.commentService.dislikeComment(commentId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your disliked the comment!");
		return message;	
	}
	
	
}
