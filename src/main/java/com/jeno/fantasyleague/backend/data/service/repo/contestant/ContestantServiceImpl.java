package com.jeno.fantasyleague.backend.data.service.repo.contestant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Initializer;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.League;

@Transactional
@Component
public class ContestantServiceImpl implements ContestantService {

	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;

	@Override
	public List<Contestant> getPossibleContestantsFromGroupStage(UefaEuro2020Initializer.Group group, League league) {
		Optional<ContestantGroup> groupOptional = contestantGroupRepository.findByNameAndLeague(group.getGroupName(), league);
		return groupOptional
				.map(group1 -> contestantGroupRepository.fetchGroupContestants(group1.getId()).stream()
						// Team not yet in eighth finals
						.filter(contestant -> gameRepository.findByLeagueAndStage(league, SoccerCupStages.EIGHTH_FINALS.toString()).stream()
									.noneMatch(game -> contestant.getId().equals(game.getHome_team_fk()) || contestant.getId().equals(game.getAway_team_fk())))
						.collect(Collectors.toList()))
				.orElse(Lists.newArrayList());
	}

}
