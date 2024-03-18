package com.jeno.fantasyleague.backend.data.service.repo.prediction;

import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.PredictionRepository;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
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
		return prediction;
	}

}
