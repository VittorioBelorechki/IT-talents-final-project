package com.vtube.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.CommentsRepository;
import com.vtube.dto.CommentDTO;
import com.vtube.dto.ContentDTO;
import com.vtube.model.Comment;
import com.vtube.model.User;
import com.vtube.model.Video;

@Service
public class CommentService {

	@Autowired
	CommentsRepository commentsRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VideoService videoService;
	
	public List<Comment> findAllByVideoId(Long videoId) {
		Video video = this.videoService.getVideoById(videoId);
		List<Comment>  comments= new LinkedList<Comment>();
		video.getComments().forEach(c -> comments.add(c));
		return comments;
	}
	
	public boolean findById(Long commentId) {
		try {
			@SuppressWarnings("unused")
			Comment comment = this.commentsRepository.findById(commentId).get();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	public List<Comment> findAllByCommentId(Long commentId) {
		return commentsRepository.findAllBySuperCommentId(commentId);
	}
	
	public void addComment(ContentDTO contentDTO, Long userId, Long videoId) {
		User author = this.userService.getUserById(userId);
		Video video = this.videoService.getVideoById(videoId);
		Comment comment = new Comment((long)0, contentDTO.getContent(), 0, 0, null, author, video);	
		this.commentsRepository.save(comment);
	}
	
	public void addSubComment(ContentDTO contentDTO, Long userId, Long commentId) {
		User author = this.userService.getUserById(userId);
		Comment superComment = this.commentsRepository.findById(commentId).get();
		Comment comment = new Comment((long)0, contentDTO.getContent(), 0, 0, superComment, author, null);
		this.commentsRepository.save(comment);
	}
	
	public void editComment(CommentDTO commentDTO) {
		Comment comment = this.commentsRepository.findById(commentDTO.getId()).get();
		comment.setContent(commentDTO.getContent());
		this.commentsRepository.save(comment);
	}

	public void deleteComment(Long commentId) {
		this.commentsRepository.deleteById(commentId);
		
	}

	public void likeComment(Long commentId) {
		Comment comment = this.commentsRepository.findById(commentId).get();
		comment.setLikes(comment.getLikes() + 1);
		this.commentsRepository.save(comment);
	}

	public void dislikeComment(Long commentId) {
		Comment comment = this.commentsRepository.findById(commentId).get();
		comment.setDislikes(comment.getDislikes() + 1);
		this.commentsRepository.save(comment);
	}

	public Comment getCommentById(Long commentId) {
		return this.commentsRepository.findById( (int)((long)commentId) ).get();
	}
	

	
}
