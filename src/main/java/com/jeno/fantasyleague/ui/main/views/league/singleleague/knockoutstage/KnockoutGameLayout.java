package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.time.LocalDateTime;
import java.util.Objects;

import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class KnockoutGameLayout extends VerticalLayout {

	protected final SingleLeagueServiceProvider singleLeagueServiceprovider;
	protected final League league;

	protected GameResultsLayout scoreWrapper;
	protected GameResultsLayout predictionWrapper;

	public KnockoutGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;

		setWidth("390px");
		setMargin(true);

//		addClassName(ValoTheme.LAYOUT_CARD);
		addClassName("bracket-game");

		HorizontalLayout wrapper = new HorizontalLayout();

		VerticalLayout teamWrapper = new VerticalLayout();
		teamWrapper.setWidth("180px");
		teamWrapper.setMargin(false);
		teamWrapper.add(Resources.getMessage("teams"));
		teamWrapper.add(createHomeTeamComponent(game));
		teamWrapper.add(createAwayTeamComponent(game));

		scoreWrapper = new GameResultsLayout(
				singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league),
				game,
				KnockoutGameBean::getHomeTeamScore,
				KnockoutGameBean::setHomeTeamScore,
				KnockoutGameBean::getAwayTeamScore,
				KnockoutGameBean::setAwayTeamScore,
				KnockoutGameBean::getHomeTeamIsWinner,
				KnockoutGameBean::setHomeTeamIsWinner);
		scoreWrapper.add(Resources.getMessage("scores"));

		predictionWrapper = new GameResultsLayout(
				LocalDateTime.now().isBefore(game.getGame().getGameDateTime()),
				game,
				KnockoutGameBean::getHomeTeamPrediction,
				KnockoutGameBean::setHomeTeamPrediction,
				KnockoutGameBean::getAwayTeamPrediction,
				KnockoutGameBean::setAwayTeamPrediction,
				KnockoutGameBean::getHomeTeamPredictionIsWinner,
				KnockoutGameBean::setHomeTeamPredictionIsWinner);
		predictionWrapper.add(Resources.getMessage("predictions"));

		if (Objects.isNull(game.getGame().getHome_team()) || Objects.isNull(game.getGame().getAway_team())) {
			scoreWrapper.setEnabled(false);
			predictionWrapper.setEnabled(false);
		}

		wrapper.add(teamWrapper);
		wrapper.add(scoreWrapper);
		wrapper.add(predictionWrapper);

		String date = DateUtil.DATE_TIME_FORMATTER.format(game.getGame().getGameDateTime());
		Label infoLabel = new Label(game.getGame().getLocation() + " <br/> " + date);
//		infoLabel.addClassName(ValoTheme.LABEL_TINY);
		infoLabel.setWidth("150px");

		add(wrapper);

		scoreWrapper.scoreChanged()
				.map(KnockoutGameBean::setScoresAndGetGameModelItem)
				.subscribe(gameModelItem -> updateKnockoutGame(singleLeagueServiceprovider, league, gameModelItem));
		predictionWrapper.scoreChanged()
				.map(KnockoutGameBean::setScoresAndGetPredictionModelItem)
				.subscribe(prediction -> {
					if (LocalDateTime.now().isBefore(game.getGame().getGameDateTime())) {
						singleLeagueServiceprovider.getPredictionRepository().saveAndFlush(prediction);
					} else {
						Notification.show(Resources.getMessage("toLateToUpdatePrediction"));
					}
					if (prediction.getHome_team_score().equals(prediction.getAway_team_score()) && Objects.isNull(prediction.getWinner())) {
						Notification.show("Be sure to select a winner");
					}
				});


		add(infoLabel);
	}

	public void updateKnockoutGame(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, Game gameBean) {
		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			singleLeagueServiceprovider.getGameService().updateKnockoutStageScore(gameBean);
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"));
		}
	}

	protected abstract HorizontalLayout createHomeTeamComponent(KnockoutGameBean game);

	protected abstract HorizontalLayout createAwayTeamComponent(KnockoutGameBean game);

}
