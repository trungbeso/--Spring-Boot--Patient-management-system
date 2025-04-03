package com.benjamin.authservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidateResetPasswordRequestToken {
	@NotBlank(message = "Token is required")
	@Size(min = 5, max = 255, message = "Token must be between 5 and 255 characters")
	private String token;
}
