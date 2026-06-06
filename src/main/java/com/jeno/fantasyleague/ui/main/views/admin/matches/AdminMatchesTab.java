package com.jeno.fantasyleague.ui.main.views.admin.matches;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2026.FifaWorldCup2026Initializer;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchPredictionBean;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdminMatchesTab extends LazyTabComponent {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;
	private final Optional<League> optionalLeague;

	private VerticalLayout layout;
	private MatchGrid matchGrid;

	public AdminMatchesTab(SingleLeagueServiceProvider singleLeagueServiceProvider, Optional<League> optionalLeague) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.optionalLeague = optionalLeague;

		initLayout();
	}

	private void initLayout() {
		layout = new VerticalLayout();
		layout.setMaxWidth("1200px");
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.setMargin(false);
		layout.setPadding(false);

		if (optionalLeague.isPresent()) {
			matchGrid = new MatchGrid(singleLeagueServiceProvider, singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(optionalLeague.get()), true);
			matchGrid.setMatches(getMatches(optionalLeague.get()));
			matchGrid.scoreChanged().subscribe(this::updateGameScoresGlobally);
			layout.add(createFilterBar(), matchGrid);
		} else {
			layout.add(new Label("Admin user has not joined a league yet"));
		}

		add(layout);
	}

	private MenuBar createFilterBar() {
		MenuBar filterBar = new MenuBar();
		filterBar.setWidthFull();
		filterBar.addThemeVariants(MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_PRIMARY);

		MenuItem showAll = filterBar.addItem(Resources.getMessage("showAllMatches"));
		showAll.addClickListener(ignored -> matchGrid.clearFilter());

		Arrays.stream(SoccerCupStages.values())
				.forEach(stage -> {
					MenuItem item = filterBar.addItem(Resources.getMessage(stage.getName()));
					item.addClickListener(ignored -> matchGrid.filterOnStage(stage));
				});

		MenuItem specificGroup = filterBar.addItem("Specific group");
		Arrays.stream(FifaWorldCup2026Initializer.groups())
				.forEach(group -> {
					MenuItem groupItem = specificGroup.getSubMenu().addItem(group.getGroupName());
					groupItem.addClickListener(ignored -> matchGrid.filterOnGroupStage(group));
				});

		return filterBar;
	}

	private List<MatchBean> getMatches(League league) {
		List<Game> games = singleLeagueServiceProvider.getGameRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(Game::getGameDateTime))
				.collect(Collectors.toList());
		Map<Long, Prediction> predictions = singleLeagueServiceProvider.getPredictionRepository()
				.findByLeagueAndUserAndJoinGames(league, singleLeagueServiceProvider.getLoggedInUser()).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity()));
		Map<Long, Contestant> contestantMap = singleLeagueServiceProvider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		List<MatchBean> matches = games.stream()
				.map(game -> new MatchBean(
						predictions.get(game.getId()),
						contestantMap.get(game.getHome_team_fk()),
						contestantMap.get(game.getAway_team_fk()),
						null,
						null,
						0.0,
						false,
						league))
				.collect(Collectors.toList());
		return matches;
	}

	public void updateGameScoresGlobally(MatchPredictionBean matchPredictionBean) {
		if (singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(matchPredictionBean.getLeague())) {
			List<Game> allGamesWithMatchNumber = singleLeagueServiceProvider.getGameRepository().findByMatchNumber(matchPredictionBean.getGame().getMatchNumber());
			allGamesWithMatchNumber.forEach(matchPredictionBean::setGameScoresAndGetGameModelItem);
			allGamesWithMatchNumber.forEach(game -> {
				if (SoccerCupStages.GROUP_PHASE.toString().equals(game.getStage())) {
					singleLeagueServiceProvider.getGameService().updateGroupStageGameScores(game);
				} else {
					singleLeagueServiceProvider.getGameService().updateKnockoutStageScore(game);
				}
			});
			// Do the set scores action on the bean as well for UI update
			matchPredictionBean.setGameScoresAndGetGameModelItem();
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"));
		}
	}
}
