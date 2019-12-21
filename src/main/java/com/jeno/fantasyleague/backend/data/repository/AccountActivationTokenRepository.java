package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.AccountActivationToken;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken, Long> {

	Optional<AccountActivationToken> findByTokenAndUser(String token, User id);

}
