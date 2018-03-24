package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.PasswordResetToken;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByTokenAndUser(String token, User id);

}
