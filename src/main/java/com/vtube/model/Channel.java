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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Table(name= "channels")
@Entity
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "name")
	@NonNull
	private String name;

	// One user can have one channel
	@OneToOne
	@JoinColumn(name="user_id", nullable = false)
	@NonNull
	private User owner;
	
	// One channel can have multiple videos
	@OneToMany(mappedBy = "owner")
	private List<Video> ownedVideos;

	// Many users can subscribe to many channels
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            }, 
            mappedBy = "subscribedChannels")
	private List<User> usersSubscribedToChannel;

}
