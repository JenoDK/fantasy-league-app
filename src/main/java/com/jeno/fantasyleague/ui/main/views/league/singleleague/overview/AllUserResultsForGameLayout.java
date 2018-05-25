package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
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

	public AllUserResultsForGameLayout(League league, UserPredictionScoreBean bean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();

		Label stageLabel = new Label(Resources.getMessage(FifaWorldCup2018Stages.valueOf(bean.getGame().getStage()).getName()));
		stageLabel.addStyleName(ValoTheme.LABEL_TINY);

		HorizontalLayout gameWrapper = new HorizontalLayout();
		gameWrapper.addComponent(GridUtil.createTeamLayout(bean.getHome_team()));
		gameWrapper.addComponent(new Label(
				OverviewUtil.getScoreWithWinner(bean.getGameHome_team_score(), bean.getGameAway_team_score(), bean.getGameHomeTeamWon())));
		gameWrapper.addComponent(GridUtil.createTeamLayout(bean.getAway_team()));

		Label dateTimeLabel = new Label(DateUtil.DATE_TIME_FORMATTER.format(bean.getGame().getGame_date_time()));
		dateTimeLabel.addStyleName(ValoTheme.LABEL_TINY);

		List<UserPredictionForGameBean> items = singleLeagueServiceprovider.getPredictionRepository().findByGameAndJoinUsersAndJoinGames(bean.getGame()).stream()
				.map(prediction -> new UserPredictionForGameBean(
						prediction.getUser(),
						prediction,
						singleLeagueServiceprovider.getLeaguePredictionScoreForUser(league, prediction, prediction.getUser()),
						Optional.ofNullable(bean.getHome_team())
								.map(Contestant::getId)
								.map(id -> id.equals(prediction.getWinner_fk())),
						OverviewUtil.isHiddenForUser(singleLeagueServiceprovider.getLoggedInUser(), league, prediction),
						bean.getPredictionHiddenUntil()))
				.collect(Collectors.toList());
		AllUserGameScoreGrid grid = new AllUserGameScoreGrid(items);

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
