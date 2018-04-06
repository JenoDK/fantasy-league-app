package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

	@Query("SELECT g FROM Game g " +
			"INNER JOIN FETCH g.home_team ht " +
			"INNER JOIN FETCH g.away_team at " +
			"WHERE g.league = :league " +
			"AND (ht.contestant_group = :contestantGroup OR at.contestant_group = :contestantGroup)")
	List<Game> findByLeagueAndJoinTeams(@Param("league") League league, @Param("contestantGroup") ContestantGroup contestantGroup);

}
