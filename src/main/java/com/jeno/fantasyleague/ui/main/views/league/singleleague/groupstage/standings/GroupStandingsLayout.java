package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.standings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.ui.VerticalLayout;

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

		setSpacing(true);
		setMargin(false);

		groupStandingsGrid = new GroupStandingsGrid();
		groupStandingsGrid.setWidth(100f, Unit.PERCENTAGE);
		groupStandingsGrid.setItems(fetchGroupContestants(singleLeagueService, league, group));

		addComponent(groupStandingsGrid);
	}

	public Set<GroupTeamBean> fetchGroupContestants(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		List<Game> allGroupGames = singleLeagueService.getGameRepository().findByLeagueAndGroupStageAndJoinTeams(league, group);
		ArrayListMultimap<Contestant, Game> gamesPerContestant = ArrayListMultimap.create();
		allGroupGames.forEach(game -> {
			gamesPerContestant.put(game.getHome_team(), game);
			gamesPerContestant.put(game.getAway_team(), game);
		});
		return gamesPerContestant.keySet().stream()
				.map(contestant -> new GroupTeamBean(
						contestant,
						getPointsInGames(gamesPerContestant.get(contestant), contestant),
						getGoalsInGames(gamesPerContestant.get(contestant), contestant)))
				.collect(Collectors.toSet());
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
