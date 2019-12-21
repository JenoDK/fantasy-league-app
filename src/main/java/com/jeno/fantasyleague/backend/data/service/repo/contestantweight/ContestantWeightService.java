package com.jeno.fantasyleague.backend.data.service.repo.contestantweight;

import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;

import java.util.List;

public interface ContestantWeightService {

	/**
	 * Adds default contestantweights for a user in a league
	 * @param league
	 * @param user
	 * @return
	 */
	List<ContestantWeight> addDefaultContestantWeights(League league, User user);

}
