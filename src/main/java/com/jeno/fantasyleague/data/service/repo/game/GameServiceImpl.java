package com.jeno.fantasyleague.data.service.repo.game;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.ContestantRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Initializer;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private ContestantRepository contestantRepository;
	@Autowired
	private ContestantGroupRepository contestantGroupRepository;

	@Override
	public void updateGroupStageGameScores(List<Game> games) {
		games.stream().forEach(this::setGroupStageWinner);
		gameRepository.saveAll(games);
		contestantRepository.saveAll(games.stream()
				.flatMap(game -> Stream.of(game.getHome_team(), game.getAway_team()))
				.collect(Collectors.toList()));
	}

	@Override
	public Game updateKnockoutStageScore(Game game) {
		Game updated = gameRepository.saveAndFlush(game);
		Set<Long> possibleContestantIdsFromGame = Sets.newHashSet();
		possibleContestantIdsFromGame.addAll(
				getOptionalGroupContestantsByPlaceHolder(game.getHome_team_placeholder(), game.getLeague()).stream()
						.map(Contestant::getId)
						.collect(Collectors.toSet()));
		possibleContestantIdsFromGame.addAll(
				getOptionalGroupContestantsByPlaceHolder(game.getAway_team_placeholder(), game.getLeague()).stream()
						.map(Contestant::getId)
						.collect(Collectors.toSet()));
		Optional<Game> nextGameOptional = Optional.ofNullable(game.getNext_game_fk())
				.flatMap(gameRepository::findById);
		if (game.getWinner() != null && nextGameOptional.isPresent()) {
			Game nextGame = nextGameOptional.get();
			Long homeTeamFk = nextGame.getHome_team_fk();
			Long awayTeamFk = nextGame.getAway_team_fk();
			boolean needsToReplaceHomeTeam = homeTeamFk != null &&
					((homeTeamFk.equals(game.getHome_team_fk()) || homeTeamFk.equals(game.getAway_team_fk())) ||
					possibleContestantIdsFromGame.contains(homeTeamFk));
			boolean needsToReplaceAwayTeam = awayTeamFk != null &&
					((awayTeamFk.equals(game.getHome_team_fk()) || awayTeamFk.equals(game.getAway_team_fk())) ||
					possibleContestantIdsFromGame.contains(awayTeamFk));
			// This game already provided a winner as the home team
			if (needsToReplaceHomeTeam) {
				nextGame.setHome_team(game.getWinner());
			// This game already provided a winner as the away team
			} else if (needsToReplaceAwayTeam) {
				nextGame.setAway_team(game.getWinner());
			// This game did not provide a winner yet and we will occupy an empty slot
			} else {
				if (homeTeamFk == null) {
					nextGame.setHome_team(game.getWinner());
				} else {
					nextGame.setAway_team(game.getWinner());
				}
			}
			gameRepository.saveAndFlush(nextGame);
		}
		return updated;
	}

	private List<Contestant> getOptionalGroupContestantsByPlaceHolder(String placeholder, League league) {
		return getGroup(placeholder)
				.flatMap(group -> contestantGroupRepository.findByNameAndLeague(group.getGroupName(), league))
				.map(g -> contestantGroupRepository.fetchGroupContestants(g.getId()))
				.orElse(Lists.newArrayList());
	}

	private void setGroupStageWinner(Game game) {
		Integer homeScore = game.getHome_team_score();
		Integer awayScore = game.getAway_team_score();
		if (homeScore > awayScore) {
			game.setWinner(game.getHome_team());
		} else if (homeScore < awayScore) {
			game.setWinner(game.getAway_team());
		} else {
			game.setWinner(null);
		}
	}

	public static Optional<FifaWorldCup2018Initializer.Group> getGroup(String placeHolder) {
		return Stream.of(FifaWorldCup2018Initializer.Group.values())
				.filter(group -> placeHolder.contains(group.getGroupName()))
				.findFirst();
	}

}
