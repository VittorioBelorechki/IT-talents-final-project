package com.vtube.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of comment object for main watch video page view. 
 * Must be returned as part of a JSON on GET -> /watch?id request
 */
@Data
@NoArgsConstructor
public class CommentDTO implements IDTO{
	@NonNull
	private Long id;
	
	@NonNull
	private String content;
	
	@NonNull
	private String userNickName;
	
	private long superCommentId;
	private int likes;
	private int dislikes;
	List<CommentDTO> subComments;
	
	public void add(CommentDTO subCommentDTO) {
		this.subComments.add(subCommentDTO);
	}
}
