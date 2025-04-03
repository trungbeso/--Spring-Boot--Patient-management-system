package com.benjamin.authservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {

	@NotBlank(message = "Email is required")
	@Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
	private String email;

}