package com.benjamin.authservice.dtos.request;

import com.benjamin.authservice.annotation.FieldsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@FieldsMatch(field = "password", fieldMatch = "passwordConfirm", message = "Password do not match")
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	@Email
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 20)
	private String password;

	private String passwordConfirm;

	@NotBlank(message = "Phone Number is required")
	@Size(min = 10, max = 20, message = "Phone Number must be between 10 and 20 characters")
	private String phoneNumber;
}
