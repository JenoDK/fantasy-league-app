package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

	Optional<League> findByGuid(String guid);

	@Query("SELECT l.owners FROM League l WHERE l.id = :id")
	List<User> fetchLeagueOwners(@Param("id") Long leagueId);

	List<League> findByTemplate(Template template);

	// league_picture is deliberately not mapped on the League entity, see League#hasLeaguePicture.
	@Query(value = "SELECT league_picture FROM league WHERE id = :id", nativeQuery = true)
	byte[] findLeaguePictureById(@Param("id") Long id);

	// Native update bypasses JPA auditing, so updated_at is bumped explicitly;
	// the same instant doubles as the image cache-buster in the UI.
	@Modifying
	@Transactional
	@Query(value = "UPDATE league SET league_picture = :picture, updated_at = :updatedAt WHERE id = :id", nativeQuery = true)
	int updateLeaguePicture(@Param("id") Long id, @Param("picture") byte[] picture, @Param("updatedAt") Instant updatedAt);
}
