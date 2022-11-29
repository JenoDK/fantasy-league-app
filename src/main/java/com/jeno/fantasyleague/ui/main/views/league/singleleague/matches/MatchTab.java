package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022.FifaWorldCup2022Initializer;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single.SingleMatchLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;

public class MatchTab extends LazyTabComponent {

	public static final Predicate<Game> TODAY_PREDICATE = game -> LocalDateTime.now().toLocalDate().equals(game.getGameDateTime().toLocalDate());
	public static final Predicate<Game> YESTERDAY_PREDICATE = game -> LocalDateTime.now().minusDays(1).toLocalDate().equals(game.getGameDateTime().toLocalDate());
	public static final Predicate<Game> TOMORROW_PREDICATE = game -> LocalDateTime.now().plusDays(1).toLocalDate().equals(game.getGameDateTime().toLocalDate());
	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final boolean loggedInUserIsAdmin;

	private MatchGrid matchGrid;
	private MatchGrid matchesToday;
	private MatchGrid matchesYesterday;
	private MatchGrid matchesTomorrow;
	private List<MenuItem> gridMenuItems = Lists.newArrayList();
	private MenuBar filterBar;
	private H2 todayMatchesLabel;
	private H2 yesterdayMatchesLabel;
	private H2 tomorrowMatchesLabel;
	private H2 allMatchesLabel;

	public MatchTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.loggedInUserIsAdmin = singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league);

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

		filterBar = new MenuBar();
		filterBar.setWidthFull();
		filterBar.addThemeVariants(MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_PRIMARY);

		matchesToday = createGrid(() -> getMatchBeans(Optional.of(TODAY_PREDICATE)));
		matchesYesterday = createGrid(() -> getMatchBeans(Optional.of(YESTERDAY_PREDICATE)));
		matchesTomorrow = createGrid(() -> getMatchBeans(Optional.of(TOMORROW_PREDICATE)));
		matchGrid = createGrid(this::getMatches);

		createFilterBar(filterBar);

		todayMatchesLabel = new H2("Matches of today");
		yesterdayMatchesLabel = new H2("Matches of yesterday");
		tomorrowMatchesLabel = new H2("Matches of tomorrow");
		allMatchesLabel = new H2("All matches");

		add(filterBar, todayMatchesLabel, matchesToday, yesterdayMatchesLabel, matchesYesterday, tomorrowMatchesLabel, matchesTomorrow);
		add(allMatchesLabel, matchGrid);
	}
	
	private MatchGrid createGrid(Supplier<List<MatchBean>> matchesProvider) {
		MatchGrid matchesGrid = new MatchGrid(singleLeagueServiceprovider, loggedInUserIsAdmin, false);
		matchesGrid.setMatches(matchesProvider.get());
		singleLeagueServiceprovider.predictionChanged(matchesGrid.predictionChanged(), prediction -> {});
		matchesGrid.scoreChanged().subscribe(singleLeagueServiceprovider::updateGameScore);
		matchesGrid.clickedMatch().subscribe(match -> {
			hideMatches();
			SingleMatchLayout singleMatchLayout = new SingleMatchLayout(match, singleLeagueServiceprovider, loggedInUserIsAdmin);
			MenuItem bacMenuItem = filterBar.addItem(VaadinIcon.ARROW_LEFT.create());
			bacMenuItem.addClickListener(ignored -> {
				remove(singleMatchLayout);
				showMatches(bacMenuItem);
			});
			add(singleMatchLayout);
		});
		return matchesGrid;
	}

	private void showMatches(MenuItem bacMenuItem) {
		matchesToday.setVisible(true);
		matchesYesterday.setVisible(true);
		matchesTomorrow.setVisible(true);
		matchGrid.setVisible(true);
		bacMenuItem.setVisible(false);
		gridMenuItems.forEach(item -> item.setVisible(true));
		todayMatchesLabel.setVisible(true);
		yesterdayMatchesLabel.setVisible(true);
		tomorrowMatchesLabel.setVisible(true);
		allMatchesLabel.setVisible(true);
	}

	private void hideMatches() {
		matchesToday.setVisible(false);
		matchesYesterday.setVisible(false);
		matchesTomorrow.setVisible(false);
		matchGrid.setVisible(false);
		gridMenuItems.forEach(item -> item.setVisible(false));
		todayMatchesLabel.setVisible(false);
		yesterdayMatchesLabel.setVisible(false);
		tomorrowMatchesLabel.setVisible(false);
		allMatchesLabel.setVisible(false);
	}

	private void createFilterBar(MenuBar filterBar) {
		MenuItem refreshItem = filterBar.addItem(VaadinIcon.REFRESH.create());
		refreshItem.addClickListener(ignored -> {
			matchGrid.setMatches(getMatches());
			matchesToday.setMatches(getMatchBeans(Optional.of(TODAY_PREDICATE)));
			matchesYesterday.setMatches(getMatchBeans(Optional.of(YESTERDAY_PREDICATE)));
			matchesTomorrow.setMatches(getMatchBeans(Optional.of(TOMORROW_PREDICATE)));
		});
		MenuItem showAllItem = filterBar.addItem(Resources.getMessage("showAllMatches"));
		showAllItem.addClickListener(ignored -> matchGrid.clearFilter());
		gridMenuItems.add(refreshItem);
		gridMenuItems.add(showAllItem);
		Arrays.stream(SoccerCupStages.values())
				.forEach(stage -> {
					MenuItem item = filterBar.addItem(Resources.getMessage(stage.getName()));
					item.addClickListener(ignored -> matchGrid.filterOnStage(stage));
					gridMenuItems.add(item);
				});
		MenuItem specificGroup = filterBar.addItem("Specific group");
		gridMenuItems.add(specificGroup);
		Arrays.stream(FifaWorldCup2022Initializer.groups())
				.forEach(group -> {
					MenuItem groupItem = specificGroup.getSubMenu().addItem(group.getGroupName());
					groupItem.addClickListener(ignored -> matchGrid.filterOnGroupStage(group));
				});
	}

	private List<MatchBean> getMatches() {
		return getMatchBeans();
	}

	private List<MatchBean> getMatchBeans() {
		return getMatchBeans(Optional.empty());
	}

	private List<MatchBean> getMatchBeans(Optional<Predicate<Game>> gamePredicate) {
		List<Game> games = singleLeagueServiceprovider.getGameRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(Game::getGameDateTime))
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
				.filter(game -> gamePredicate.map(predicate -> predicate.test(game)).orElse(true))
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
