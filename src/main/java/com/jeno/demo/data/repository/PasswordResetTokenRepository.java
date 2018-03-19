package com.jeno.demo.data.repository;

import com.jeno.demo.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByUser(Long id);

	Optional<PasswordResetToken> findByToken(String token);

}
