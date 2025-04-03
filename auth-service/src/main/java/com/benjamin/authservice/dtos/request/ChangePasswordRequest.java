package com.benjamin.authservice.dtos.request;

import com.benjamin.authservice.annotation.FieldsMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsMatch(field = "password", fieldMatch = "passwordConfirm", message = "password must match")
public class ChangePasswordRequest {
	private String password;
	private String passwordConfirm;
	private String token;
}
