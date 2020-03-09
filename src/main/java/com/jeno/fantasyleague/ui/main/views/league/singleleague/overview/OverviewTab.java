package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OverviewTab extends VerticalLayout {

	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final League league;

	public OverviewTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		addClassName("overview-tab");
		setMargin(false);
		setPadding(false);
		setSpacing(false);

		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;

		UpcomingMatchesGrid upcomingMatchesGrid = new UpcomingMatchesGrid();
		upcomingMatchesGrid.setItems(getUpcomingMatches(singleLeagueServiceprovider));
		upcomingMatchesGrid.viewAllResultsClicked().subscribe(bean -> new PopupWindow.Builder(
				"All scores",
				window ->
				new AllUserResultsForGameLayout(league, bean, singleLeagueServiceprovider))
//				.closable(true)
//				.resizable(true)
				.setHeight(700)
				.setWidth(900)
				.build()
				.open());
		add(upcomingMatchesGrid);

//		List<UserTotalScoreBean> scoreBeans = fetchTotalScores();
//		Set<String> userNames = scoreBeans.stream()
//				.map(UserTotalScoreBean::getUser)
//				.map(User::getUsername)
//				.collect(Collectors.toSet());
//
//		UserTotalScoreGrid totalScoreGrid = new UserTotalScoreGrid(scoreBeans, false, singleLeagueServiceprovider.getLoggedInUser());
//		totalScoreGrid.setWidth("100%");
//
//		Accordion predictionScoresLayout = new Accordion();
//		predictionScoresLayout.getElement().getClassList().add("darker-tabcolor");
//
//		Map<FifaWorldCup2018Stages, UserPredictionScoresGrid> gridPerStageMap = Maps.newHashMap();
//		Arrays.stream(FifaWorldCup2018Stages.values())
//				.forEach(stage -> gridPerStageMap.put(stage, new UserPredictionScoresGrid()));
//
//		gridPerStageMap.keySet().stream()
//				.sorted(Comparator.comparingInt(FifaWorldCup2018Stages::getSeq))
//				.forEach(key -> predictionScoresLayout.add(Resources.getMessage(key.getName()), gridPerStageMap.get(key)));
//
//		Observable.merge(gridPerStageMap.values().stream().map(UserPredictionScoresGrid::viewAllResultsClicked).collect(Collectors.toSet()))
//				.subscribe(bean -> new PopupWindow.Builder(
//							"All scores",
//							"allScoresWindows", window ->
//							new AllUserResultsForGameLayout(league, bean, singleLeagueServiceprovider))
////						.closable(true)
////						.resizable(true)
//						.setHeight(700)
//						.setWidth(900)
//						.build()
//						.open());
//
//		totalScoreGrid.addItemClickListener(event ->
//				setPredictionScoreItems(fetchPredictionScores(event.getItem().getUser()), gridPerStageMap));
//		scoreBeans.stream()
//				.filter(bean -> singleLeagueServiceprovider.getLoggedInUser().getId().equals(bean.getUser().getId()))
//				.findFirst()
//				.ifPresent(totalScoreGrid::select);
//		setPredictionScoreItems(fetchPredictionScores(singleLeagueServiceprovider.getLoggedInUser()), gridPerStageMap);
//
//		Button refreshButton = new Button(VaadinIcon.REFRESH.create());
////		refreshButton.addClassName(ValoTheme.BUTTON_TINY);
////		refreshButton.addClassName(ValoTheme.BUTTON_ICON_ONLY);
//		refreshButton.addClickListener(ignored -> {
//			List<UserTotalScoreBean> scores = fetchTotalScores();
//			totalScoreGrid.setItems(scores);
//
//			scores.stream()
//					.filter(bean -> singleLeagueServiceprovider.getLoggedInUser().getId().equals(bean.getUser().getId()))
//					.findFirst()
//					.ifPresent(totalScoreGrid::select);
//			setPredictionScoreItems(fetchPredictionScores(singleLeagueServiceprovider.getLoggedInUser()), gridPerStageMap);
//		});
//
//		add(refreshButton);
//		add(totalScoreGrid);
//		add(predictionScoresLayout);
	}

	private void setPredictionScoreItems(List<UserPredictionScoreBean> userPredictionScoreBeans, Map<SoccerCupStages, UpcomingMatchesGrid> gridPerStageMap) {
		ArrayListMultimap<SoccerCupStages, UserPredictionScoreBean> beansPerStage = ArrayListMultimap.create();
		userPredictionScoreBeans.forEach(bean -> beansPerStage.put(SoccerCupStages.valueOf(bean.getGame().getStage()), bean));
		beansPerStage.asMap().entrySet().forEach(entry -> gridPerStageMap.get(entry.getKey()).setItems(entry.getValue()));
	}

	private List<UserPredictionScoreBean> getUpcomingMatches(SingleLeagueServiceProvider singleLeagueServiceProvider) {
		LocalDateTime date1 = LocalDateTime.now().minusHours(12);
		List<Game> games = singleLeagueServiceProvider.getGameRepository().findByLeagueAndGameDateTimeGreaterThan(league, date1).stream()
				.sorted(Comparator.comparing(Game::getGameDateTime))
				.collect(Collectors.toList());
		Map<Long, Prediction> predictions = singleLeagueServiceProvider.getPredictionRepository()
				.findByLeagueAndUserAndJoinGames(league, singleLeagueServiceProvider.getLoggedInUser()).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity()));
		Map<Long, Contestant> contestantMap = singleLeagueServiceProvider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		Map<Long, Integer> weightsForUserPerContestant =
				singleLeagueServiceProvider.getContestantWeightRepository().findByUserAndLeague(
						singleLeagueServiceProvider.getLoggedInUser(),
						league).stream()
						.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		return games.stream()
				.map(game -> new UserPredictionScoreBean(
						predictions.get(game.getId()),
						contestantMap.get(game.getHome_team_fk()),
						contestantMap.get(game.getAway_team_fk()),
						weightsForUserPerContestant.get(game.getHome_team_fk()),
						weightsForUserPerContestant.get(game.getAway_team_fk()),
						singleLeagueServiceProvider.getLeaguePredictionScoreForUser(league, predictions.get(game.getId()), singleLeagueServiceProvider.getLoggedInUser()),
						OverviewUtil.isHiddenForUser(singleLeagueServiceProvider.getLoggedInUser(), league, predictions.get(game.getId())),
						league))
				.sorted(Comparator.comparing(o -> o.getGame().getGameDateTime()))
				.collect(Collectors.toList());
	}

	private List<UserPredictionScoreBean> fetchPredictionScores(User user) {
		Map<Long, Contestant> contestantMap = singleLeagueServiceprovider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		List<ContestantWeight> contestantWeights = singleLeagueServiceprovider.getContestantWeightRepository().findByUserAndLeague(user, league);
		Map<Long, Integer> weightsForUserPerContestant =
				contestantWeights.stream()
						.collect(Collectors.toMap(ContestantWeight::getContestant_fk, ContestantWeight::getWeight));
		List<Prediction> predictionsWithJoinedGames =
				singleLeagueServiceprovider.getPredictionRepository().findByLeagueAndUserAndJoinGames(league, user);
		Map<Long, Double> scorePerPredictionMap =
				singleLeagueServiceprovider.getLeaguePredictionScoresForUser(league, predictionsWithJoinedGames, contestantWeights);
		return predictionsWithJoinedGames.stream()
				.map(prediction -> new UserPredictionScoreBean(
						prediction,
						contestantMap.get(prediction.getGame().getHome_team_fk()),
						contestantMap.get(prediction.getGame().getAway_team_fk()),
						weightsForUserPerContestant.get(prediction.getGame().getHome_team_fk()),
						weightsForUserPerContestant.get(prediction.getGame().getAway_team_fk()),
						scorePerPredictionMap.get(prediction.getId()),
						OverviewUtil.isHiddenForUser(singleLeagueServiceprovider.getLoggedInUser(), league, prediction),
						league))
				.collect(Collectors.toList());
	}

	public List<UserTotalScoreBean> fetchTotalScores() {
		return UserTotalScoreBean.transfer(singleLeagueServiceprovider.getUserLeagueScores(league));
	}

}
