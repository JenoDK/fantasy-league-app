package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueMessageRepository extends JpaRepository<LeagueMessage, Long> {

	List<LeagueMessage> findByLeague(League league);
}
