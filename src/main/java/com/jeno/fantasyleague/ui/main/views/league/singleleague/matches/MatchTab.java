package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.LeagueMenuBar;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single.SingleMatchLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MatchTab extends LazyTabComponent {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final boolean loggedInUserIsAdmin;
	private final LeagueMenuBar menuBar;

	private MatchGrid matchGrid;
	private List<MenuItem> gridMenuItems = Lists.newArrayList();

	public MatchTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, LeagueMenuBar menuBar) {
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.loggedInUserIsAdmin = singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league);
		this.menuBar = menuBar;

		initLayout();
	}

	@Override
	protected void hide() {
		super.hide();
		gridMenuItems.forEach(item -> item.setVisible(false));
	}

	@Override
	protected void show() {
		super.show();
		gridMenuItems.forEach(item -> item.setVisible(true));
	}

	private void initLayout() {
		setMargin(false);
		setPadding(false);

		matchGrid = new MatchGrid(singleLeagueServiceprovider, loggedInUserIsAdmin);
		matchGrid.setMatches(getMatches());
		MenuItem refreshItem = menuBar.addItem(VaadinIcon.REFRESH.create());
		refreshItem.addClickListener(ignored -> matchGrid.setMatches(getMatches()));
		MenuItem showAllItem = menuBar.addItem(Resources.getMessage("showAllMatches"));
		showAllItem.addClickListener(ignored -> matchGrid.clearFilter());
		gridMenuItems.add(refreshItem);
		gridMenuItems.add(showAllItem);
		Arrays.stream(SoccerCupStages.values())
				.forEach(stage -> {
					MenuItem item = menuBar.addItem(Resources.getMessage(stage.getName()));
					item.addClickListener(ignored -> matchGrid.filterOnStage(stage));
					gridMenuItems.add(item);
				});
		singleLeagueServiceprovider.predictionChanged(matchGrid.predictionChanged(), prediction -> {});
		matchGrid.scoreChanged().subscribe(singleLeagueServiceprovider::updateGameScore);
		matchGrid.clickedMatch().subscribe(match -> {
			matchGrid.setVisible(false);
			gridMenuItems.forEach(item -> item.setVisible(false));
			SingleMatchLayout singleMatchLayout = new SingleMatchLayout(match, singleLeagueServiceprovider, loggedInUserIsAdmin);
			MenuItem bacMenuItem = menuBar.addItem(VaadinIcon.ARROW_LEFT.create());
			bacMenuItem.addClickListener(ignored -> {
				remove(singleMatchLayout);
				matchGrid.setVisible(true);
				bacMenuItem.setVisible(false);
				gridMenuItems.forEach(item -> item.setVisible(true));
			});
			add(singleMatchLayout);
		});

		add(matchGrid);
	}

	private List<MatchBean> getMatches() {
		List<Game> games = singleLeagueServiceprovider.getGameRepository().findByLeague(league).stream()
				.sorted((g1, g2) -> {
					LocalDateTime now = LocalDateTime.now();
					boolean isPast1 = g1.getGameDateTime().isBefore(now);
					boolean isPast2 = g2.getGameDateTime().isBefore(now);

					if (isPast1 != isPast2) {
						return isPast1 ? 1 : -1;
					}

					return isPast1 ? g2.getGameDateTime().compareTo(g1.getGameDateTime()) : g1.getGameDateTime().compareTo(g2.getGameDateTime());
				})
				.collect(Collectors.toList());
		Map<Long, Prediction> predictions = singleLeagueServiceprovider.getPredictionRepository()
				.findByLeagueAndUserAndJoinGames(league, singleLeagueServiceprovider.getLoggedInUser()).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity()));
		Map<Long, Contestant> contestantMap = singleLeagueServiceprovider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		List<ContestantWeight> weightsForUser = singleLeagueServiceprovider.getContestantWeightRepository().findByUserAndLeague(
				singleLeagueServiceprovider.getLoggedInUser(),
				league);
		Map<Long, Integer> weightsForUserPerContestant =
				weightsForUser.stream()
						.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		Map<Long, Double> scoreForUserPerGame = singleLeagueServiceprovider.getLeaguePredictionScoresForUser(
				league,
				Lists.newArrayList(predictions.values()),
				weightsForUser);
		List<MatchBean> matches = games.stream()
				.map(game -> new MatchBean(
						predictions.get(game.getId()),
						contestantMap.get(game.getHome_team_fk()),
						contestantMap.get(game.getAway_team_fk()),
						weightsForUserPerContestant.get(game.getHome_team_fk()),
						weightsForUserPerContestant.get(game.getAway_team_fk()),
						scoreForUserPerGame.get(game.getId()),
						OverviewUtil.isHiddenForUser(singleLeagueServiceprovider.getLoggedInUser(), league, predictions.get(game.getId())),
						league))
				.collect(Collectors.toList());
		return matches.stream()
//				.limit(7)
				.collect(Collectors.toList());
	}
}
