package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

	List<Prediction> findByGameInAndUser(List<Game> games, User user);

	@Query("SELECT p FROM Prediction p " +
			"INNER JOIN FETCH p.game g " +
			"WHERE g.league = :league " +
			"AND p.user = :user")
	List<Prediction> findByLeagueAndUserAndJoinGames(@Param("league") League league, @Param("user") User user);
}
