package com.jeno.fantasyleague.ui.main.views.league.singleleague.upcomingmatches;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class UpcomingMatchesTab extends VerticalLayout {

	public UpcomingMatchesTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		addGamesGrids(league, singleLeagueService);
	}

	private void addGamesGrids(League league, SingleLeagueServiceProvider singleLeagueService) {
		singleLeagueService.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.forEach(group -> {
					ListDataProvider<Game> dataProvider = createGroupGamesDataProvider(singleLeagueService, league, group);
					Label groupLabel = new Label(group.getName(), ContentMode.HTML);
					groupLabel.addStyleName(ValoTheme.LABEL_H3);
					GamesGrid grid = new GamesGrid(dataProvider);
					addComponents(groupLabel, grid);
				});
	}

	private ListDataProvider<Game> createGroupGamesDataProvider(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		return DataProvider.fromStream(singleLeagueService.getGameRepository().findByLeagueAndJoinTeams(league, group).stream());
	}

	private Set<Game> extractGames(ContestantGroup group, Set<Game> allGames) {
		return group.getContestants().stream()
				.flatMap(contestant -> allGames.stream()
						.filter(game -> contestant.equals(game.getHome_team()) || contestant.equals(game.getAway_team())))
				.collect(Collectors.toSet());
	}

}
