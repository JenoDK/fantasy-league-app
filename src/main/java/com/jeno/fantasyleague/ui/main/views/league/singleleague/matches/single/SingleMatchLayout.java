package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchCard;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SingleMatchLayout extends VerticalLayout {

	private final MatchBean match;
	private final SingleLeagueServiceProvider singleLeagueServiceprovider;

	public SingleMatchLayout(MatchBean match, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this.match = match;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;

		initLayout();
	}

	private void initLayout() {
		setPadding(false);
		setMargin(false);

		MatchCard matchCard = new MatchCard(match, null);
		add(matchCard);

		Map<Long, Integer> homeTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(match.getHomeTeam(), match.getLeague()).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));
		Map<Long, Integer> awayTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(match.getAwayTeam(), match.getLeague()).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));
		List<UserScoresForGameBean> items = singleLeagueServiceprovider.getPredictionRepository().findByGameAndJoinUsersAndJoinGames(match.getGame()).stream()
				.map(prediction -> new UserScoresForGameBean(
						prediction.getUser(),
						prediction,
						singleLeagueServiceprovider.getLeaguePredictionScoreForUser(match.getLeague(), prediction, prediction.getUser()),
						homeTeamWeights.get(prediction.getUser().getId()),
						awayTeamWeights.get(prediction.getUser().getId()),
						Optional.ofNullable(match.getHomeTeam())
								.filter(ignored -> Objects.nonNull(prediction.getWinner()))
								.map(Contestant::getId)
								.map(id -> id.equals(prediction.getWinner_fk())),
						OverviewUtil.isHiddenForUser(singleLeagueServiceprovider.getLoggedInUser(), match.getLeague(), prediction),
						match.getPredictionHiddenUntil()))
				.sorted(Comparator.comparing(UserScoresForGameBean::getScore).reversed())
				.collect(Collectors.toList());
		UserScoresForGameGrid grid = new UserScoresForGameGrid(match, items, singleLeagueServiceprovider.getLoggedInUser());
		add(grid);

		singleLeagueServiceprovider.predictionChanged(matchCard.predictionChanged(), prediction -> {
			items.stream()
					.filter(b -> b.getPrediction().getId().equals(prediction.getId()))
					.forEach(b -> {
						b.setPrediction(prediction);
						match.setPrediction(prediction);
						grid.getDataProvider().refreshItem(b);
					});
		});
	}
}
