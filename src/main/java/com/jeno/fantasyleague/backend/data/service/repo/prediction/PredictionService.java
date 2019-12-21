package com.jeno.fantasyleague.backend.data.service.repo.prediction;

import java.util.List;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

public interface PredictionService {

	List<Prediction> addDefaultPredictions(League league, User user);

}
