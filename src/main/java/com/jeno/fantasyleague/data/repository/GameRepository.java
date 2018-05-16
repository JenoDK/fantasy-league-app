package com.jeno.fantasyleague.data.repository;

import java.util.List;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

	List<Game> findByLeague(League league);

	@Query("SELECT g FROM Game g " +
			"INNER JOIN FETCH g.home_team ht " +
			"INNER JOIN FETCH g.away_team at " +
			"WHERE g.league = :league " +
			"AND g.stage = 'GROUP_PHASE' " +
			"AND (ht.contestant_group = :contestantGroup OR at.contestant_group = :contestantGroup)")
	List<Game> findByLeagueAndGroupStageAndJoinTeams(@Param("league") League league, @Param("contestantGroup") ContestantGroup contestantGroup);

	List<Game> findByLeagueAndStage(League league, String stage);
}
