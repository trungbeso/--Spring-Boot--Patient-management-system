package com.benjamin.authservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(SamePasswordException.class)
	public ResponseEntity<Map<String, String>> handlePassword(SamePasswordException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", "New Password must different from old password");
		errors.put("error", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(TokenExpirationException.class)
	public ResponseEntity<Map<String, String>> handleTokenExpirationException(TokenExpirationException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", "Your token has expired");
		errors.put("error", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", "Invalid token");
		errors.put("error", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(er ->
			  errors.put(er.getField(), er.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}
}
