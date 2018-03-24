package com.jeno.demo.data.repository;

import com.jeno.demo.model.AccountActivationToken;
import com.jeno.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken, Long> {

	Optional<AccountActivationToken> findByTokenAndUser(String token, User id);

}
