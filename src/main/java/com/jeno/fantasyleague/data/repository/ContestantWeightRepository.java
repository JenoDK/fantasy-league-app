package com.jeno.fantasyleague.data.repository;

import java.util.List;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantWeightRepository extends JpaRepository<ContestantWeight, Long> {

	@Query("SELECT cw FROM ContestantWeight cw " +
			"INNER JOIN FETCH cw.contestant c " +
			"WHERE cw.user = :user " +
			"AND cw.league = :league")
	List<ContestantWeight> findByUserAndLeagueAndJoinContestant(
			@Param("user") User user,
			@Param("league") League league);

	List<ContestantWeight> findByUserAndLeague(User user, League league);

	List<ContestantWeight> findByContestantAndLeague(Contestant contestant, League league);

}
