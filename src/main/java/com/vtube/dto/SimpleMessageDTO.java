package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**Simple text object which can be returned as JSON 
 */
@Data
@NoArgsConstructor
public class SimpleMessageDTO implements IDTO{
	@NonNull
	private String message;
}
