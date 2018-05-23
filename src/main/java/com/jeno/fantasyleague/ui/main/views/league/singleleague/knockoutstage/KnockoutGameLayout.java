package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.time.LocalDateTime;
import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class KnockoutGameLayout extends VerticalLayout {

	protected final SingleLeagueServiceProvider singleLeagueServiceprovider;
	protected final League league;

	protected GameResultsLayout scoreWrapper;
	protected GameResultsLayout predictionWrapper;

	public KnockoutGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;

		setWidth(390f, Unit.PIXELS);
		setMargin(true);

		addStyleName(ValoTheme.LAYOUT_CARD);
		addStyleName("bracket-game");

		HorizontalLayout wrapper = new HorizontalLayout();

		VerticalLayout teamWrapper = new VerticalLayout();
		teamWrapper.setWidth(180f, Unit.PIXELS);
		teamWrapper.setMargin(false);
		teamWrapper.setCaption(Resources.getMessage("teams"));
		teamWrapper.addComponent(createHomeTeamComponent(game));
		teamWrapper.addComponent(createAwayTeamComponent(game));

		scoreWrapper = new GameResultsLayout(
				singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league),
				game,
				KnockoutGameBean::getHomeTeamScore,
				KnockoutGameBean::setHomeTeamScore,
				KnockoutGameBean::getAwayTeamScore,
				KnockoutGameBean::setAwayTeamScore,
				KnockoutGameBean::getHomeTeamIsWinner,
				KnockoutGameBean::setHomeTeamIsWinner);
		scoreWrapper.setCaption(Resources.getMessage("scores"));

		predictionWrapper = new GameResultsLayout(
				LocalDateTime.now().isBefore(game.getGame().getGame_date_time()),
				game,
				KnockoutGameBean::getHomeTeamPrediction,
				KnockoutGameBean::setHomeTeamPrediction,
				KnockoutGameBean::getAwayTeamPrediction,
				KnockoutGameBean::setAwayTeamPrediction,
				KnockoutGameBean::getHomeTeamPredictionIsWinner,
				KnockoutGameBean::setHomeTeamPredictionIsWinner);
		predictionWrapper.setCaption(Resources.getMessage("predictions"));

		wrapper.addComponent(teamWrapper);
		wrapper.addComponent(scoreWrapper);
		wrapper.addComponent(predictionWrapper);

		String date = DateUtil.DATE_TIME_FORMATTER.format(game.getGame().getGame_date_time());
		Label infoLabel = new Label(game.getGame().getLocation() + " <br/> " + date, ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);
		infoLabel.setWidth(150f, Unit.PIXELS);

		addComponent(wrapper);

		scoreWrapper.scoreChanged()
				.map(KnockoutGameBean::setScoresAndGetGameModelItem)
				.subscribe(gameModelItem -> updateKnockoutGame(singleLeagueServiceprovider, league, gameModelItem));
		predictionWrapper.scoreChanged()
				.map(KnockoutGameBean::setScoresAndGetPredictionModelItem)
				.subscribe(prediction -> {
					if (LocalDateTime.now().isBefore(game.getGame().getGame_date_time())) {
						singleLeagueServiceprovider.getPredictionRepository().saveAndFlush(prediction);
					} else {
						Notification.show(Resources.getMessage("toLateToUpdatePrediction"), Notification.Type.WARNING_MESSAGE);
					}
				});


		addComponent(infoLabel);
	}

	public void updateKnockoutGame(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, Game gameBean) {
		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			singleLeagueServiceprovider.getGameService().updateKnockoutStageScore(gameBean);
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"), Notification.Type.WARNING_MESSAGE);
		}
	}

	protected abstract HorizontalLayout createHomeTeamComponent(KnockoutGameBean game);

	protected abstract HorizontalLayout createAwayTeamComponent(KnockoutGameBean game);

	protected HorizontalLayout createTeamComponent(Contestant contestant, String teamPlaceHolder) {
		return Optional.ofNullable(contestant)
				.map(GridUtil::createTeamLayout)
				.orElse(new HorizontalLayout(new Label(teamPlaceHolder)));
	}
}
