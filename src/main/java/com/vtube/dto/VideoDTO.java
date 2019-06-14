package com.vtube.dto;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is representation of video object. 
 * Must be returned as list of videos  on GET -> //results?search= request
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VideoDTO extends SmallVideoDTO {

	@NonNull
	private Long channelId;

	private String description;

	private LocalDate dateOfCreation;
	
	public VideoDTO(@NonNull Long id, @NonNull String channelName, @NonNull String title, int numberOfViews, @NonNull String thumbnailUrl, 
			Long channelId, String description, LocalDate dateOfCreation) {
		super(id, channelName, title, numberOfViews, thumbnailUrl);
		this.channelId = channelId;
		this.description = description;
		this.dateOfCreation = dateOfCreation;
	}

}
