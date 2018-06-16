package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.AllUserResultsForGameLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserPredictionScoreBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserPredictionScoresGrid;
import com.jeno.fantasyleague.util.ImageUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
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
		addStyleName("existing");
		Label name = new Label(league.getName(), ContentMode.HTML);
		name.addStyleName(ValoTheme.LABEL_H3);
		addComponent(name);

		Image leagueImage = new Image();
		leagueImage.addStyleName("league-picture");
		leagueImage.setSource(ImageUtil.getLeaguePictureResource(league));
		addComponent(leagueImage);

		UserPredictionScoresGrid upcomingMatchesGrid = new UserPredictionScoresGrid();
		upcomingMatchesGrid.setCaption("Upcoming matches");
		upcomingMatchesGrid.setItems(getUpcomingMatches());
		upcomingMatchesGrid.viewAllResultsClicked().subscribe(bean -> new PopupWindow.Builder(
				"All scores",
				"allScoresWindows", window ->
				new AllUserResultsForGameLayout(league, bean, singleLeagueServiceProvider))
				.closable(true)
				.resizable(true)
				.setHeight(700)
				.setWidth(900)
				.build()
				.show());
		addComponent(upcomingMatchesGrid);

		setExpandRatio(name, 1);
		setExpandRatio(leagueImage, 4);
		setExpandRatio(upcomingMatchesGrid, 6);
		setComponentAlignment(name, Alignment.TOP_CENTER);
		setComponentAlignment(leagueImage, Alignment.TOP_CENTER);
	}

	private List<UserPredictionScoreBean> getUpcomingMatches() {
		LocalDateTime date1 = LocalDateTime.now().minusMinutes(90);
		LocalDateTime date2 = LocalDateTime.now().plusDays(1);
		List<Game> games = singleLeagueServiceProvider.getGameRepository().findByLeagueAndGameDateTimeBetween(league, date1, date2);
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
