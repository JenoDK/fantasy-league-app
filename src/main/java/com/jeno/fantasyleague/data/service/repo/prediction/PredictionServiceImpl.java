package com.jeno.fantasyleague.data.service.repo.prediction;

import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.PredictionRepository;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Component
public class PredictionServiceImpl implements PredictionService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private PredictionRepository predictionRepository;

	@Override
	public List<Prediction> addDefaultPredictions(League league, User user) {
		List<Prediction> predictions = gameRepository.findByLeague(league).stream()
				.map(game -> createDefaultPrediction(game, user))
				.collect(Collectors.toList());
		return predictionRepository.saveAll(predictions);
	}

	private Prediction createDefaultPrediction(Game game, User user) {
		Prediction prediction = new Prediction();
		prediction.setGame(game);
		prediction.setUser(user);
		prediction.setAway_team_score(0);
		prediction.setHome_team_score(0);
		return prediction;
	}
}
