package com.jeno.demo.data.repository;

import com.jeno.demo.model.PasswordResetToken;
import com.jeno.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByTokenAndUser(String token, User id);

}
