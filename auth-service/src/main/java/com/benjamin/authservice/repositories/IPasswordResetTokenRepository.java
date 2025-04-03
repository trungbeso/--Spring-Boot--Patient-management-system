package com.benjamin.authservice.repositories;

import com.benjamin.authservice.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
	Optional<PasswordResetToken> findByEmail(String email);

	void deleteByHashedTokenId(String hashedTokenId);
}
