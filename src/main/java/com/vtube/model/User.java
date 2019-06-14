package com.vtube.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Table(name="users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "nick_name", nullable = false, unique = true)
	@NonNull
	private String nickName;
	
	@Column(name = "profile_picture_url")
	private String profilePicture;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	@NonNull
	private String email;

	@Column(name = "password", nullable = false, columnDefinition = "LONGTEXT")
	@NonNull
	@JsonIgnore
	private String password;

	@Column(name = "age", nullable = false)
	private int age;
	
	//One user can have one channel
	@OneToOne(mappedBy = "owner")
	private Channel ownedChannel;
	
	//One user can have many comments
	@OneToMany(mappedBy = "author")
	private List<Comment> comments;
	
	//User have videos he likes
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(
    		name= "liked_videos",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private List<Video> likedVideos;
    
    //Many users can subscribe to many channels
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(
    		name= "subscribed_channels",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "channel_id"))
    private List<Channel> subscribedChannels;
    
    //History of watched videos for each user
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(
    		name= "videos_history",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private List<Video> watchedVideos;
    
    //Users can have videos for watching later
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(
    		name= "watch_later",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private List<Video> videosForLater;
}
