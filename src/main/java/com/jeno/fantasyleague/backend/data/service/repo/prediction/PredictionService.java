package com.jeno.fantasyleague.backend.data.service.repo.prediction;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;

import java.util.List;

public interface PredictionService {

	List<Prediction> addDefaultPredictions(League league, User user);

}
