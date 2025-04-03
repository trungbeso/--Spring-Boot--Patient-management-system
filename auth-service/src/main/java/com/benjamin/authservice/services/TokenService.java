package com.benjamin.authservice.services;


import com.benjamin.authservice.exceptions.TokenExpirationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenService {

	@NonFinal
	@Value("${jwt.secret}")
	private String secretKey;

	@NonFinal
	@Value("${jwt.expire.time}")
	private Integer expireTime;

	public String generateToken(Authentication authentication) {
		String role = authentication.getAuthorities()
			  .stream()
			  .map(Objects::toString)
			  .collect(Collectors.joining(","));

		return generateAccessToken(authentication.getName(), role);
	}

	private String generateAccessToken(String email, String role) {
		LocalDateTime expireAt = LocalDateTime.now().plusSeconds(expireTime);

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		Date expiration = Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			  .subject(email)
			  .claim("role", role)
			  .expiration(expiration)
			  .signWith(key)
			  .compact();
	}

	public Authentication getAuthentication(String token) {
		if (token == null) {
			throw new TokenExpirationException("Token is null");
		}

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

		try {
			Claims claims = Jwts.parser()
				  .verifyWith(key)
				  .build()
				  .parseSignedClaims(token)
				  .getPayload();

			String role = claims.get("role").toString();

			Set<GrantedAuthority> authorities = Set.of(role.split(",")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

			User principalUser = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principalUser, token, authorities);
		} catch (Exception e) {
			throw new RuntimeException("UNAUTHORIZED TOKEN");
		}

	}
}
