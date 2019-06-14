package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of user page view. Must be returned as JSON on GET -> /user/id request
 */
@Data
@NoArgsConstructor
public class UserDTO implements IDTO{
	@NonNull
	private Long id;
	
	@NonNull
	private String nickName;
	
	private Long numberOfSubscribers;
	
	private String profilePicture;
	
	private int numberOfOwnVideos;
	private int numberOfLikedVideos;
}
