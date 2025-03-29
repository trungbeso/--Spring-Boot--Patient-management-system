package com.benjamin.patientservice.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String s) {
		super(s);
	}
}
