package com.benjamin.authservice.exceptions;

public class TokenExpirationException extends RuntimeException {
	public TokenExpirationException(String message) {
		super(message);
	}
}
