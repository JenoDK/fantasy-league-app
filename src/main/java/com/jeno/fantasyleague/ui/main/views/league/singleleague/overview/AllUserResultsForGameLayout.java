package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single.UserScoresForGameBean;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AllUserResultsForGameLayout extends VerticalLayout {

	public AllUserResultsForGameLayout(
			League league,
			UserPredictionScoreBean bean,
			SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();

		Label stageLabel = new Label(Resources.getMessage(SoccerCupStages.valueOf(bean.getGame().getStage()).getName()));
//		stageLabel.addClassName(ValoTheme.LABEL_TINY);

		HorizontalLayout gameWrapper = new HorizontalLayout();
		gameWrapper.add(LayoutUtil.createTeamLayout(bean.getHome_team(), bean.getGame().getHome_team_placeholder()));
		gameWrapper.add(new Label(
				OverviewUtil.getScoreWithWinner(bean.getGameHome_team_score(), bean.getGameAway_team_score(), bean.getGameHomeTeamWon())));
		gameWrapper.add(LayoutUtil.createTeamLayout(bean.getAway_team(), bean.getGame().getAway_team_placeholder()));

		Label dateTimeLabel = new Label(DateUtil.DATE_TIME_FORMATTER.format(bean.getGame().getGameDateTime()));
//		dateTimeLabel.addClassName(ValoTheme.LABEL_TINY);

		Map<Long, Integer> homeTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(bean.getHome_team(), league).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));
		Map<Long, Integer> awayTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(bean.getAway_team(), league).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));

		List<UserScoresForGameBean> items = singleLeagueServiceprovider.getPredictionRepository().findByGameAndJoinUsersAndJoinGames(bean.getGame()).stream()
				.map(prediction -> new UserScoresForGameBean(
						prediction.getUser(),
						prediction,
						singleLeagueServiceprovider.getLeaguePredictionScoreForUser(league, prediction, prediction.getUser()),
						homeTeamWeights.get(prediction.getUser().getId()),
						awayTeamWeights.get(prediction.getUser().getId()),
						Optional.ofNullable(bean.getHome_team())
								.filter(ignored -> Objects.nonNull(prediction.getWinner()))
								.map(Contestant::getId)
								.map(id -> id.equals(prediction.getWinner_fk())),
						OverviewUtil.isHiddenForUser(singleLeagueServiceprovider.getLoggedInUser(), league, prediction),
						bean.getPredictionHiddenUntil()))
				.collect(Collectors.toList());
//		UserScoresForGameGrid grid = new UserScoresForGameGrid(bean, items, singleLeagueServiceprovider.getLoggedInUser());
//		grid.setWidth("90%");

		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setMargin(false);
		wrapper.add(stageLabel);
		wrapper.add(gameWrapper);
		wrapper.add(dateTimeLabel);
//		wrapper.setComponentAlignment(stageLabel, Alignment.TOP_CENTER);
//		wrapper.setComponentAlignment(gameWrapper, Alignment.TOP_CENTER);
//		wrapper.setComponentAlignment(dateTimeLabel, Alignment.TOP_CENTER);

		add(wrapper);
//		add(grid);
//		setComponentAlignment(wrapper, Alignment.TOP_CENTER);
//		setComponentAlignment(grid, Alignment.TOP_CENTER);
	}

}
