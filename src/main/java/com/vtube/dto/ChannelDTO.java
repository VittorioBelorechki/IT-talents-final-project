package com.vtube.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of channel page view. Must be returned as JSON on GET -> /channel?id=(something) request
 */
@Data
@NoArgsConstructor
public class ChannelDTO implements IDTO{
	@NonNull
	private Long id;
	
	@NonNull
	private String name;
	
	int numberOfSubscribers;
	
	List<SmallVideoDTO> ownedVideos;
}
