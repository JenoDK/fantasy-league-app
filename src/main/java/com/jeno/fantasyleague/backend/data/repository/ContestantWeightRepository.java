package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestantWeightRepository extends JpaRepository<ContestantWeight, Long> {

	@Query("SELECT cw FROM ContestantWeight cw " +
			"INNER JOIN FETCH cw.contestant c " +
			"WHERE cw.user = :user " +
			"AND cw.league = :league")
	List<ContestantWeight> findByUserAndLeagueAndJoinContestant(
			@Param("user") User user,
			@Param("league") League league);

	@Query("SELECT cw FROM ContestantWeight cw " +
			"INNER JOIN FETCH cw.contestant c " +
			"WHERE cw.league = :league")
	List<ContestantWeight> findByLeagueAndJoinContestant(@Param("league") League league);

	List<ContestantWeight> findByUserAndLeague(User user, League league);

	List<ContestantWeight> findByContestantAndLeague(Contestant contestant, League league);

}
