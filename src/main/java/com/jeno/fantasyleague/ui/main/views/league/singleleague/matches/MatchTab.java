package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single.SingleMatchLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.subjects.PublishSubject;

public class MatchTab extends VerticalLayout {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final PublishSubject<ClickEvent<MenuItem>> back;
	private final MenuItem backItem;

	private MatchGrid matchGrid;

	public MatchTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, PublishSubject<ClickEvent<MenuItem>> back, MenuItem backItem) {
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.back = back;
		this.backItem = backItem;

		initLayout();
	}

	private void initLayout() {
		setMargin(false);
		setPadding(false);

		matchGrid = new MatchGrid();
		matchGrid.setMatches(getMatches());
		singleLeagueServiceprovider.predictionChanged(matchGrid.predictionChanged(), prediction -> {});
		matchGrid.clickedMatch().subscribe(match -> {
			matchGrid.setVisible(false);
			backItem.setVisible(true);
			SingleMatchLayout singleMatchLayout = new SingleMatchLayout(match, singleLeagueServiceprovider);
			back.subscribe(ignored -> {
				remove(singleMatchLayout);
				backItem.setVisible(false);
				matchGrid.setVisible(true);
			});
			add(singleMatchLayout);
		});

		add(matchGrid);
	}

	private List<MatchBean> getMatches() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("Fetching all matches");
		List<Game> games = singleLeagueServiceprovider.getGameRepository().findByLeague(league).stream()
				.sorted((g1, g2) -> {
					LocalDateTime now = LocalDateTime.now();
					boolean isPast1 = g1.getGameDateTime().isBefore(now);
					boolean isPast2 = g2.getGameDateTime().isBefore(now);

					if (isPast1 != isPast2)
						return isPast1 ? 1 : -1;

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
		stopWatch.stop();
		System.err.println("Took " + stopWatch.getTotalTimeMillis() + "ms");
		return matches;
	}
}
