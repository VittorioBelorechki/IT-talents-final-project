package com.vtube.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="comments")
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
	@NonNull
	private String content;
	
	@Column(name = "likes", nullable = false)
	private int likes = 0;
	
	@Column(name = "dislikes", nullable = false)
	private int dislikes = 0;
	
	//Comment may have parent comment on which it is written
	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name= "super_comment_id"))
	private Comment superComment;
	
	//Every comment have an author
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	@NonNull
	private User author;
	
	//Every comment have video on which it is written
	@ManyToOne
	@JoinColumn(name="video_id", nullable = false)
	private Video commentedVideo;
}
