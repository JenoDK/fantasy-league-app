package com.jeno.fantasyleague.data.service.repo.contestant;

import java.util.List;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Initializer;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.League;

public interface ContestantService {

	List<Contestant> getPossibleContestantsFromGroupStage(FifaWorldCup2018Initializer.Group group, League league);

}
