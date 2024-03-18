package com.jeno.fantasyleague.ui.main.views.league.singleleague.groups;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.backend.model.*;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupStandingsLayout extends VerticalLayout {

	private GroupStandingsGrid groupStandingsGrid;
	private SingleLeagueServiceProvider singleLeagueService;
	private League league;
	private ContestantGroup group;

	public GroupStandingsLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		this.singleLeagueService = singleLeagueService;
		this.league = league;
		this.group = group;

		setSpacing(false);
		setMargin(false);
		setPadding(false);

		H2 groupLabel = new H2(group.getName());

		groupStandingsGrid = new GroupStandingsGrid();
		groupStandingsGrid.setWidth(100f, Unit.PERCENTAGE);
		groupStandingsGrid.setItems(fetchGroupContestants(singleLeagueService, league, group));

		add(groupLabel);
		add(groupStandingsGrid);
	}

	public Set<GroupTeamBean> fetchGroupContestants(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		List<Game> allGroupGames = singleLeagueService.getGameRepository().findByLeagueAndGroupStageAndJoinTeams(league, group);
		ArrayListMultimap<Contestant, Game> gamesPerContestant = ArrayListMultimap.create();
		allGroupGames.forEach(game -> {
			gamesPerContestant.put(game.getHome_team(), game);
			gamesPerContestant.put(game.getAway_team(), game);
		});
		List<Game> games = new ArrayList<>();
		games.addAll(gamesPerContestant.values());
		Map<Long, Prediction> predictionPerGame = new HashMap<>();
		for (Prediction prediction : singleLeagueService.getLoggedInUserPredictions(games)) {
			predictionPerGame.put(prediction.getGame_fk(), prediction);
		}
		return gamesPerContestant.keySet().stream()
				.map(contestant -> {
					List<Game> gamesForContestant = gamesPerContestant.get(contestant);
					Map<Long, Game> gamesToId = gamesForContestant.stream()
							.collect(Collectors.toMap(Game::getId, Function.identity()));
					List<Prediction> predictionsForContestant = gamesForContestant.stream()
							.map(Game::getId)
							.map(predictionPerGame::get)
							.collect(Collectors.toList());
					return new GroupTeamBean(
							contestant,
							getPointsInGames(gamesForContestant, contestant),
							getGoalsInGames(gamesForContestant, contestant),
							getPredictedGoals(predictionsForContestant, gamesToId, contestant),
							getPredictedPoints(predictionsForContestant, contestant));
				})
				.collect(Collectors.toSet());
	}

	private Integer getPredictedPoints(List<Prediction> predictionsForContestant, Contestant contestant) {
		return predictionsForContestant.stream()
				.filter(prediction -> Objects.nonNull(prediction.getHome_team_score()) && Objects.nonNull(prediction.getAway_team_score()))
				.mapToInt(prediction -> {
					if (prediction.getWinner_fk() == null) {
						return 1;
					} else if (prediction.getWinner_fk().equals(contestant.getId())) {
						return 3;
					} else {
						return 0;
					}
				})
				.sum();
	}

	private Integer getPredictedGoals(List<Prediction> predictionsForContestant, Map<Long, Game> gamesToId, Contestant contestant) {
		return predictionsForContestant.stream()
				.mapToInt(prediction -> {
					Game game = gamesToId.get(prediction.getGame_fk());
					if (game.getHome_team_fk().equals(contestant.getId())) {
						return Optional.ofNullable(prediction.getHome_team_score()).orElse(0);
					} else {
						return Optional.ofNullable(prediction.getAway_team_score()).orElse(0);
					}
				})
				.sum();
	}

	private Integer getGoalsInGames(List<Game> games, Contestant contestant) {
		return games.stream()
				.mapToInt(game -> {
					if (game.getHome_team_fk().equals(contestant.getId())) {
						return Optional.ofNullable(game.getHome_team_score()).orElse(0);
					} else {
						return Optional.ofNullable(game.getAway_team_score()).orElse(0);
					}
				})
				.sum();
	}

	private Integer getPointsInGames(List<Game> games, Contestant contestant) {
		return games.stream()
				.filter(game -> Objects.nonNull(game.getHome_team_score()) && Objects.nonNull(game.getAway_team_score()))
				.mapToInt(game -> {
					if (game.getHome_team_score() > game.getAway_team_score()) {
						return game.getHome_team_fk().equals(contestant.getId()) ? 3 : 0;
					} else if (game.getHome_team_score() == game.getAway_team_score()) {
						return 1;
					} else {
						return game.getAway_team_fk().equals(contestant.getId()) ? 3 : 0;
					}
				})
				.sum();
	}

	public void refresh() {
		groupStandingsGrid.setItems(fetchGroupContestants(singleLeagueService, league, group));
	}
}
