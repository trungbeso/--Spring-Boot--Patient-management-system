package com.benjamin.authservice.controllers;

import com.benjamin.authservice.dtos.request.ChangePasswordRequest;
import com.benjamin.authservice.dtos.request.PasswordResetRequest;
import com.benjamin.authservice.dtos.request.ValidateResetPasswordRequestToken;
import com.benjamin.authservice.services.AuthService;
import com.benjamin.authservice.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forgotPassword")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ForgotPasswordController {

	AuthService authService;
	PasswordService passwordService;

	@PostMapping("/request-reset-password")
	@Operation(summary = "Request reset password", description = "Request reset password API")
	public ResponseEntity<?> requestResetPassword(@Valid @RequestBody PasswordResetRequest request
		  , BindingResult bindingResult) {
		//validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}
		// request reset password
		var result = authService.handleResetPassword(request);
		return ResponseEntity.ok().body("password reset successful" + result);
	}

	@PostMapping("/validate-reset-password-token")
	@Operation(summary = "Validate reset password token", description = "Validate reset password token API")
	public ResponseEntity<?> validateResetPasswordToken(@Valid @RequestBody ValidateResetPasswordRequestToken request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}

		try {
			boolean result = passwordService.validatePasswordResetToken(request.getToken());
			return ResponseEntity.ok().body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/change-password")
	@Operation(summary = "Change password", description = "Change password API")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, BindingResult bindingResult) {
		//validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}
		//change password
		var result = authService.changePassword(request);
		return ResponseEntity.ok().body(result);
	}
}
