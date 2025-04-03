package com.benjamin.authservice.services;

import com.benjamin.authservice.exceptions.InvalidTokenException;
import com.benjamin.authservice.exceptions.TokenExpirationException;
import com.benjamin.authservice.models.PasswordResetToken;
import com.benjamin.authservice.repositories.IPasswordResetTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordEncoder passwordEncoder;
	private final IPasswordResetTokenRepository passwordResetTokenRepository;

	@NonFinal
	@Value("${jwt.secret}")
	private String secretKey;

	public String hashPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	public String getEmailFromPasswordResetToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		Claims claims = Jwts.parser()
			  .verifyWith(key)
			  .build()
			  .parseSignedClaims(token)
			  .getPayload();

		return claims.getSubject();
	}

	public String generatePasswordResetToken(String email) {
		String jsonTokenId = UUID.randomUUID().toString();
		LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);
		Date expiration = Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant());

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		String jsonWebToken = Jwts.builder()
			  .subject(email)
			  .id(jsonTokenId)
			  .expiration(expiration)
			  .signWith(key)
			  .compact();

		String hashedTokenId = passwordEncoder.encode(jsonTokenId);

		PasswordResetToken token = new PasswordResetToken(
			  hashedTokenId,
			  email,
			  ZonedDateTime.now().plusMinutes(15)
		);
		passwordResetTokenRepository.save(token);

		return jsonWebToken;
	}

	public boolean validatePasswordResetToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		Claims claims;
		try {
			claims = Jwts.parser()
				  .verifyWith(key)
				  .build()
				  .parseSignedClaims(token)
				  .getPayload();
		} catch (JwtException e) {
			throw new InvalidTokenException("Invalid token");
		}

		String jsonTokenId = claims.getId();
		String email = claims.getSubject();

		PasswordResetToken resetToken = passwordResetTokenRepository.findByEmail(email)
			  .orElseThrow(() -> new TokenExpirationException("Token not found"));

		if (!passwordEncoder.matches(jsonTokenId, resetToken.getHashedTokenId())) {
			throw new InvalidTokenException("Invalid token");
		}

		if (resetToken.getExpiration().isBefore(ZonedDateTime.now())) {
			throw new TokenExpirationException("Expired token");
		}

		return true;
	}
}
