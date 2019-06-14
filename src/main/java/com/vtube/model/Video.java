package com.vtube.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Table(name="videos")
@Entity
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "title", nullable = false, columnDefinition = "TEXT")
	@NonNull
	private String title;
	
	@Column(name = "thumbnail", nullable = false, columnDefinition = "TEXT")
	@NonNull
	private String thumbnail;
	
	@Column(name = "url", nullable = false, columnDefinition = "TEXT")
	@NonNull
	private String url;

	@Column(name="description", columnDefinition = "LONGTEXT")
	private String description;
	
	@Column(name = "date_of_creation", nullable = false, columnDefinition = "DATE")
	@NonNull
	private LocalDate dateOfCreation;
    
    //History of users who not like this video
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(
    		name= "dislikes_of_videos",
    		joinColumns= @JoinColumn(name= "video_id"),
    		inverseJoinColumns= @JoinColumn(name= "user_id"))
    private List<User> usersWhoDisLikeThisVideo;
	
	//one video can have many comments
	@OneToMany(mappedBy = "commentedVideo")
	private List<Comment> comments;
	
	//one channel can have multiple videos
	@ManyToOne
	@JoinColumn(name="channel_id", nullable = false)
	@NonNull
	private Channel owner;
	
	//one video can be liked by many users
	//User have videos he likes
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            }, 
            mappedBy = "likedVideos")
    private List<User> usersWhoLikeThisVideo;

	private int numberOfViews = 0;
}
