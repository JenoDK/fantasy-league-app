package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

	Optional<League> findByGuid(String guid);

	@Query("SELECT l.owners FROM League l WHERE l.id = :id")
	List<User> fetchLeagueOwners(@Param("id") Long leagueId);

	List<League> findByTemplate(Template template);
}
