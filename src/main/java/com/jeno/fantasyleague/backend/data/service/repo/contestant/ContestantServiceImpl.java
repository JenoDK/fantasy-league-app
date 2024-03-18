package com.jeno.fantasyleague.backend.data.service.repo.contestant;

import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Initializer;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantGroup;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Component
public class ContestantServiceImpl implements ContestantService {

	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;

	@Override
	public List<Contestant> getPossibleContestantsFromGroupStage(List<UefaEuro2020Initializer.Group> groups, League league) {
		List<String> names = groups.stream()
				.map(UefaEuro2020Initializer.Group::getGroupName)
				.collect(Collectors.toList());
		List<ContestantGroup> groupsEntities = contestantGroupRepository.findByNameInAndLeague(names, league);
		List<Game> eightFinalGames = gameRepository.findByLeagueAndStage(league, SoccerCupStages.EIGHTH_FINALS.toString());
		return groupsEntities.stream()
				.flatMap(group -> contestantGroupRepository.fetchGroupContestants(group.getId()).stream()
						// Team not yet in eighth finals
//						.filter(contestant -> eightFinalGames.stream()
//									.noneMatch(game -> contestant.getId().equals(game.getHome_team_fk()) || contestant.getId().equals(game.getAway_team_fk())))
				)
				.collect(Collectors.toList());
	}

}
