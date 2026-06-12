package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByEmail(String email);

	Optional<User> findByExternalAuthId(String externalAuthId);

	Optional<User> findByUsername(String username);

	Boolean existsByEmail(String email);

	// profile_picture is deliberately not mapped on the User entity, see User#hasProfilePicture.
	@Query(value = "SELECT profile_picture FROM users WHERE id = :id", nativeQuery = true)
	byte[] findProfilePictureById(@Param("id") Long id);

	// Native update bypasses JPA auditing, so updated_at is bumped explicitly;
	// the same instant doubles as the image cache-buster in the UI.
	@Modifying
	@Transactional
	@Query(value = "UPDATE users SET profile_picture = :picture, updated_at = :updatedAt WHERE id = :id", nativeQuery = true)
	int updateProfilePicture(@Param("id") Long id, @Param("picture") byte[] picture, @Param("updatedAt") Instant updatedAt);
}
