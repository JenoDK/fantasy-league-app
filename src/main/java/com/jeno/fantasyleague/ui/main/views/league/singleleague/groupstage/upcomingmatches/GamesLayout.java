package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GamesLayout extends VerticalLayout {

	private GamesGrid gamesGrid;
	private SingleLeagueServiceProvider singleLeagueService;
	private League league;
	private ContestantGroup group;

	public GamesLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		this.singleLeagueService = singleLeagueService;
		this.league = league;
		this.group = group;

		setSpacing(true);
		setMargin(false);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H2);

		gamesGrid = new GamesGrid(league, singleLeagueService);
		gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));

		gamesGrid.predictionChanged()
				.filter(gameBean -> {
					boolean isInTime = LocalDateTime.now().isBefore(gameBean.getGame_date_time());
					if (!isInTime) {
						Notification.show(Resources.getMessage("toLateToUpdatePrediction"), Notification.Type.WARNING_MESSAGE);
					}
					return isInTime;
				})
				.map(GameBean::setPredictionsAndGetModelItem)
				.subscribe(prediction -> singleLeagueService.getPredictionRepository().saveAndFlush(prediction));

		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			gamesGrid.scoreChanged()
					.map(GameBean::setTeamScoresAndGetModelItem)
					.subscribe(gameBean -> saveGameScores(singleLeagueService, league, Lists.newArrayList(gameBean)));
		}

		addComponent(gamesGrid);
	}

	public void saveGameScores(SingleLeagueServiceProvider singleLeagueService, League league, List<Game> changedGames) {
		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			singleLeagueService.getGameService().updateGroupStageGameScores(changedGames);
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"), Notification.Type.WARNING_MESSAGE);
		}
	}

	private List<GameBean> getGameBeans(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		Map<Long, Game> games = singleLeagueService.getGameRepository().findByLeagueAndGroupStageAndJoinTeams(league, group).stream()
				.collect(Collectors.toMap(Game::getId, Function.identity()));
		return singleLeagueService.getLoggedInUserPredictions(Lists.newArrayList(games.values())).stream()
				// Like this because we want the Contestants in the game entity.
				.map(prediction -> new GameBean(games.get(prediction.getGame().getId()), prediction))
				.sorted(Comparator.comparing(GameBean::getGame_date_time))
				.collect(Collectors.toList());
	}

	public void refresh() {
		gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));
	}
}
