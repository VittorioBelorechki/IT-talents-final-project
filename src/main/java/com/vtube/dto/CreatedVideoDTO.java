package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of created video object. 
 * Must be returned as JSON to POST -> /videos request
 */
@Data
@NoArgsConstructor
public class CreatedVideoDTO {
	@NonNull
	private Long id;
	
	@NonNull
	private String title;
	
	@NonNull
	private String url;
	
	private String description;
}
