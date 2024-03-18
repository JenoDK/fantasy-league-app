package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.fifaworld2022.FifaWorldCup2022Initializer;
import com.jeno.fantasyleague.backend.model.*;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MatchTab extends LazyTabComponent {

	public static final Predicate<Game> TODAY_PREDICATE = game -> LocalDateTime.now().toLocalDate().equals(game.getGameDateTime().toLocalDate());
	public static final Predicate<Game> YESTERDAY_PREDICATE = game -> LocalDateTime.now().minusDays(1).toLocalDate().equals(game.getGameDateTime().toLocalDate());
	public static final Predicate<Game> TOMORROW_PREDICATE = game -> LocalDateTime.now().plusDays(1).toLocalDate().equals(game.getGameDateTime().toLocalDate());
	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final User user;
	private final boolean userIsAdmin;
	private final boolean isForAdminModule;

	private MatchGrid allMatchesGrid;
	private MatchGrid matchesTodayGrid;
	private MatchGrid matchesYesterdayGrid;
	private MatchGrid matchesTomorrowGrid;
	private List<MenuItem> gridMenuItems = Lists.newArrayList();
	private MenuBar filterBar;
	private H2 todayMatchesLabel;
	private H2 yesterdayMatchesLabel;
	private H2 tomorrowMatchesLabel;
	private H2 allMatchesLabel;

	public MatchTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, boolean isForAdminModule, User user) {
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.user = user;
		this.userIsAdmin = isForAdminModule || singleLeagueServiceprovider.userIsLeagueAdmin(league, user);
		this.isForAdminModule = isForAdminModule;

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

		List<MatchBean> matchesToday = getMatchBeans(Optional.of(TODAY_PREDICATE));
		List<MatchBean> matchesYesterday = getMatchBeans(Optional.of(YESTERDAY_PREDICATE));
		List<MatchBean> matchesTomorrow = getMatchBeans(Optional.of(TOMORROW_PREDICATE));

		createFilterBar(filterBar);
		add(filterBar);
		if (!matchesToday.isEmpty()) {
			todayMatchesLabel = new H2("Matches of today");
			matchesTodayGrid = createGrid(() -> matchesToday);
			add(todayMatchesLabel, matchesTodayGrid);
		}
		if (!matchesYesterday.isEmpty()) {
			yesterdayMatchesLabel = new H2("Matches of yesterday");
			matchesYesterdayGrid = createGrid(() -> matchesYesterday);
			add(yesterdayMatchesLabel, matchesYesterdayGrid);
		}
		if (!matchesTomorrow.isEmpty()) {
			tomorrowMatchesLabel = new H2("Matches of tomorrow");
			matchesTomorrowGrid = createGrid(() -> matchesTomorrow);
			add(tomorrowMatchesLabel, matchesTomorrowGrid);
		}

		allMatchesGrid = createGrid(this::getMatches);
		allMatchesLabel = new H2("All matches");
		add(allMatchesLabel, allMatchesGrid);
	}
	
	private MatchGrid createGrid(Supplier<List<MatchBean>> matchesProvider) {
		MatchGrid matchesGrid = new MatchGrid(singleLeagueServiceprovider, userIsAdmin, isForAdminModule);
		matchesGrid.setMatches(matchesProvider.get());
		singleLeagueServiceprovider.predictionChanged(matchesGrid.predictionChanged(), isForAdminModule, prediction -> {});
		matchesGrid.scoreChanged().subscribe(singleLeagueServiceprovider::updateGameScore);
		matchesGrid.clickedMatch().subscribe(match -> {
			hideMatches();
			SingleMatchLayout singleMatchLayout = new SingleMatchLayout(match, singleLeagueServiceprovider, userIsAdmin);
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
		if (matchesTodayGrid != null) {
			matchesTodayGrid.setVisible(true);
		}
		if (matchesYesterdayGrid != null) {
			matchesYesterdayGrid.setVisible(true);
		}
		if (matchesTomorrowGrid != null) {
			matchesTomorrowGrid.setVisible(true);
		}
		if (todayMatchesLabel != null) {
			todayMatchesLabel.setVisible(true);
		}
		if (yesterdayMatchesLabel != null) {
			yesterdayMatchesLabel.setVisible(true);
		}
		if (tomorrowMatchesLabel != null) {
			tomorrowMatchesLabel.setVisible(true);
		}
		allMatchesGrid.setVisible(true);
		bacMenuItem.setVisible(false);
		gridMenuItems.forEach(item -> item.setVisible(true));
		allMatchesLabel.setVisible(true);
	}

	private void hideMatches() {
		if (matchesTodayGrid != null) {
			matchesTodayGrid.setVisible(false);
		}
		if (matchesYesterdayGrid != null) {
			matchesYesterdayGrid.setVisible(false);
		}
		if (matchesTomorrowGrid != null) {
			matchesTomorrowGrid.setVisible(false);
		}
		if (todayMatchesLabel != null) {
			todayMatchesLabel.setVisible(false);
		}
		if (yesterdayMatchesLabel != null) {
			yesterdayMatchesLabel.setVisible(false);
		}
		if (tomorrowMatchesLabel != null) {
			tomorrowMatchesLabel.setVisible(false);
		}
		allMatchesGrid.setVisible(false);
		gridMenuItems.forEach(item -> item.setVisible(false));
		allMatchesLabel.setVisible(false);
	}

	private void createFilterBar(MenuBar filterBar) {
		MenuItem refreshItem = filterBar.addItem(VaadinIcon.REFRESH.create());
		refreshItem.addClickListener(ignored -> {
			allMatchesGrid.setMatches(getMatches());
			matchesTodayGrid.setMatches(getMatchBeans(Optional.of(TODAY_PREDICATE)));
			matchesYesterdayGrid.setMatches(getMatchBeans(Optional.of(YESTERDAY_PREDICATE)));
			matchesTomorrowGrid.setMatches(getMatchBeans(Optional.of(TOMORROW_PREDICATE)));
		});
		MenuItem showAllItem = filterBar.addItem(Resources.getMessage("showAllMatches"));
		showAllItem.addClickListener(ignored -> allMatchesGrid.clearFilter());
		gridMenuItems.add(refreshItem);
		gridMenuItems.add(showAllItem);
		Arrays.stream(SoccerCupStages.values())
				.forEach(stage -> {
					MenuItem item = filterBar.addItem(Resources.getMessage(stage.getName()));
					item.addClickListener(ignored -> allMatchesGrid.filterOnStage(stage));
					gridMenuItems.add(item);
				});
		MenuItem specificGroup = filterBar.addItem("Specific group");
		gridMenuItems.add(specificGroup);
		Arrays.stream(FifaWorldCup2022Initializer.groups())
				.forEach(group -> {
					MenuItem groupItem = specificGroup.getSubMenu().addItem(group.getGroupName());
					groupItem.addClickListener(ignored -> allMatchesGrid.filterOnGroupStage(group));
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
				.findByLeagueAndUserAndJoinGames(league, user).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity()));
		Map<Long, Contestant> contestantMap = singleLeagueServiceprovider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		List<ContestantWeight> weightsForUser = singleLeagueServiceprovider.getContestantWeightRepository().findByUserAndLeague(
				user,
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
						OverviewUtil.isHiddenForUser(user, league, predictions.get(game.getId())),
						league))
				.collect(Collectors.toList());
		return matches.stream()
//				.limit(7)
				.collect(Collectors.toList());
	}
}
