package com.jeno.fantasyleague.backend.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

	@Query("SELECT l.owners FROM League l WHERE l.id = :id")
	List<User> fetchLeagueOwners(@Param("id") Long leagueId);

}
