package com.benjamin.authservice.exceptions;

public class SamePasswordException extends RuntimeException {
	public SamePasswordException(String message) {
		super(message);
	}
}
