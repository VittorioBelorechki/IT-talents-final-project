package com.vtube.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of video object in list of videos for the watch video page view. 
 * Must be returned as a part of a JSON on GET -> /watch?id request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmallVideoDTO implements IDTO{
	@NonNull
	private Long id;
	
	@NonNull
	private String channelName;
	
	@NonNull
	private String title;
	
	private int numberOfViews;
	
	@NonNull
	private String thumbnail;
	
}
