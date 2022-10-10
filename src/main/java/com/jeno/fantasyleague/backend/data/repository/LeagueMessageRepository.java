package com.jeno.fantasyleague.backend.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueMessage;

@Repository
public interface LeagueMessageRepository extends JpaRepository<LeagueMessage, Long> {

	List<LeagueMessage> findByLeague(League league);
}
