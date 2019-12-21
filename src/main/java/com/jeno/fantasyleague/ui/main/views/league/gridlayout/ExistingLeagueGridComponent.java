package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.AllUserResultsForGameLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserPredictionScoreBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserPredictionScoresGrid;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import io.reactivex.Observable;

public class ExistingLeagueGridComponent extends AbstractLeagueGridComponent {

	private final League league;

	private SingleLeagueServiceProvider singleLeagueServiceProvider;

	public ExistingLeagueGridComponent(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		this.league = league;
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;

		createLeagueComponent(league);
	}

	private void createLeagueComponent(League league) {
		addClassName("existing");
		Label name = new Label(league.getName());
//		name.addClassName(ValoTheme.LABEL_H3);
		add(name);

		Image leagueImage = ImageUtil.getLeaguePictureResource(league);
		leagueImage.addClassName("league-picture");
		add(leagueImage);

		UserPredictionScoresGrid upcomingMatchesGrid = new UserPredictionScoresGrid();
		// TODO
//		upcomingMatchesGrid.setCaption("Upcoming/recent matches");
		upcomingMatchesGrid.setItems(getUpcomingMatches());
		upcomingMatchesGrid.viewAllResultsClicked().subscribe(bean -> new PopupWindow.Builder(
				"All scores",
				"allScoresWindows", window ->
				new AllUserResultsForGameLayout(league, bean, singleLeagueServiceProvider))
//				.closable(true)
//				.resizable(true)
				.setHeight(700)
				.setWidth(900)
				.build()
				.open());
		add(upcomingMatchesGrid);

//		setExpandRatio(name, 1);
//		setExpandRatio(leagueImage, 4);
//		setExpandRatio(upcomingMatchesGrid, 6);
//		setComponentAlignment(name, Alignment.TOP_CENTER);
//		setComponentAlignment(leagueImage, Alignment.TOP_CENTER);
	}

	private List<UserPredictionScoreBean> getUpcomingMatches() {
		LocalDateTime date1 = LocalDateTime.now().minusHours(12);
		List<Game> games = singleLeagueServiceProvider.getGameRepository().findByLeagueAndGameDateTimeGreaterThan(league, date1).stream()
				.sorted(Comparator.comparing(Game::getGameDateTime))
				.limit(5)
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
				.collect(Collectors.toList());
	}

	public Observable<League> click() {
		return RxUtil.clicks(this).map(ignored -> league);
	}

}
