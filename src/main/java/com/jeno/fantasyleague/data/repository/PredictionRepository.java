package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

	List<Prediction> findByGameInAndUser(List<Game> games, User user);

}
