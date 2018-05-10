package com.jeno.fantasyleague.data.service.repo.prediction;

import java.util.List;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;

public interface PredictionService {

	List<Prediction> addDefaultPredictions(League league, User user);

}
