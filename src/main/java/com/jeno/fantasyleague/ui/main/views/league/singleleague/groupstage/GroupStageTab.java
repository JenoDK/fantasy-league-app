package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Comparator;

public class GroupStageTab extends VerticalLayout {

	private TabSheet tabSheet;
	private VerticalLayout upcomingMatchesLayout;
	private VerticalLayout predictionsLayout;

	public GroupStageTab(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		setMargin(false);
		setSpacing(false);

		tabSheet = new TabSheet();

		upcomingMatchesLayout = new VerticalLayout();
		initGamesGrids(league, singleLeagueService);
		tabSheet.addTab(upcomingMatchesLayout, "Upcoming games");

		predictionsLayout = new VerticalLayout();
		tabSheet.addTab(predictionsLayout, "My Predictions");

		addComponent(tabSheet);
	}

	private void initGamesGrids(League league, SingleLeagueServiceProvider singleLeagueService) {
		singleLeagueService.getContestantGroupRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(ContestantGroup::getName))
				.forEach(group -> {
					ListDataProvider<Game> dataProvider = createGroupGamesDataProvider(singleLeagueService, league, group);
					Label groupLabel = new Label(group.getName(), ContentMode.HTML);
					groupLabel.addStyleName(ValoTheme.LABEL_H3);
					upcomingMatchesLayout.addComponents(groupLabel, new GamesGrid(dataProvider));
				});
	}

	private ListDataProvider<Game> createGroupGamesDataProvider(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		return DataProvider.fromStream(singleLeagueService.getGameRepository().findByLeagueAndJoinTeams(league, group).stream());
	}

}
