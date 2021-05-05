package com.jeno.fantasyleague.backend.data.service.repo.contestant;

import java.util.List;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Initializer;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.League;

public interface ContestantService {

	List<Contestant> getPossibleContestantsFromGroupStage(List<UefaEuro2020Initializer.Group> groups, League league);

}
