package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpcomingMatchesTab extends VerticalLayout {

	public UpcomingMatchesTab(League league) {
		super();
		setMargin(false);
		setSpacing(false);

		addGamesGrids(league);
	}

	private void addGamesGrids(League league) {
		Map<ContestantGroup, Set<Game>> gamesPerGroup = league.getContestantGroups().stream()
				.collect(Collectors.toMap(Function.identity(), group -> extractGames(group, league.getGames())));
		gamesPerGroup.entrySet().stream()
				.sorted(Comparator.comparing(entry -> entry.getKey().getName()))
				.forEach(entry -> {
					Label groupLabel = new Label(entry.getKey().getName(), ContentMode.HTML);
					groupLabel.addStyleName(ValoTheme.LABEL_H3);
					GamesGrid grid = new GamesGrid(entry.getValue());
					addComponents(groupLabel, grid);
				});
	}

	private Set<Game> extractGames(ContestantGroup group, Set<Game> allGames) {
		return group.getContestants().stream()
				.flatMap(contestant -> allGames.stream()
						.filter(game -> contestant.equals(game.getHome_team()) || contestant.equals(game.getAway_team())))
				.collect(Collectors.toSet());
	}

}
