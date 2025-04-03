package com.benjamin.authservice.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "password_reset_tokens", indexes = {
	  @Index(name = "idx_password_reset_tokens_email", columnList = "email")
})
public class PasswordResetToken {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	UUID id;

	@Column(nullable = false, unique = true)
	String hashedTokenId;

	@Column(nullable = false)
	String email;

	@TimeZoneStorage(TimeZoneStorageType.NATIVE)
	@Column(columnDefinition = "DATETIMEOFFSET")
	ZonedDateTime expiration;

	public PasswordResetToken(String hashedTokenId, String email, ZonedDateTime expiration) {
		this.hashedTokenId = hashedTokenId;
		this.email = email;
		this.expiration = expiration;
	}
}
