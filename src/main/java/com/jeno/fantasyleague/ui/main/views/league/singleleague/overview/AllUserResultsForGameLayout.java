package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AllUserResultsForGameLayout extends VerticalLayout {

	public AllUserResultsForGameLayout(
			League league,
			UserPredictionScoreBean bean,
			SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();

		Label stageLabel = new Label(Resources.getMessage(FifaWorldCup2018Stages.valueOf(bean.getGame().getStage()).getName()));
		stageLabel.addStyleName(ValoTheme.LABEL_TINY);

		HorizontalLayout gameWrapper = new HorizontalLayout();
		gameWrapper.addComponent(GridUtil.createTeamLayout(bean.getHome_team(), bean.getGame().getHome_team_placeholder()));
		gameWrapper.addComponent(new Label(
				OverviewUtil.getScoreWithWinner(bean.getGameHome_team_score(), bean.getGameAway_team_score(), bean.getGameHomeTeamWon())));
		gameWrapper.addComponent(GridUtil.createTeamLayout(bean.getAway_team(), bean.getGame().getAway_team_placeholder()));

		Label dateTimeLabel = new Label(DateUtil.DATE_TIME_FORMATTER.format(bean.getGame().getGameDateTime()));
		dateTimeLabel.addStyleName(ValoTheme.LABEL_TINY);

		Map<Long, Integer> homeTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(bean.getHome_team(), league).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));
		Map<Long, Integer> awayTeamWeights = singleLeagueServiceprovider.getContestantWeightRepository()
				.findByContestantAndLeague(bean.getAway_team(), league).stream()
				.collect(Collectors.toMap(ContestantWeight::getUser_fk, ContestantWeight::getWeight));

		List<UserPredictionForGameBean> items = singleLeagueServiceprovider.getPredictionRepository().findByGameAndJoinUsersAndJoinGames(bean.getGame()).stream()
				.map(prediction -> new UserPredictionForGameBean(
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
		AllUserGameScoreGrid grid = new AllUserGameScoreGrid(bean.getHome_team(), bean.getAway_team(), items, singleLeagueServiceprovider.getLoggedInUser());
		grid.setWidth(90, Unit.PERCENTAGE);

		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setMargin(false);
		wrapper.addComponent(stageLabel);
		wrapper.addComponent(gameWrapper);
		wrapper.addComponent(dateTimeLabel);
		wrapper.setComponentAlignment(stageLabel, Alignment.TOP_CENTER);
		wrapper.setComponentAlignment(gameWrapper, Alignment.TOP_CENTER);
		wrapper.setComponentAlignment(dateTimeLabel, Alignment.TOP_CENTER);

		addComponent(wrapper);
		addComponent(grid);
		setComponentAlignment(wrapper, Alignment.TOP_CENTER);
		setComponentAlignment(grid, Alignment.TOP_CENTER);
	}

}
