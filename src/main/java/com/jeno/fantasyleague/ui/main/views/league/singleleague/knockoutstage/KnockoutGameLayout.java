package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.subjects.BehaviorSubject;

public abstract class KnockoutGameLayout extends VerticalLayout {

	private final BehaviorSubject<Boolean> scoreChanged = BehaviorSubject.create();

	protected final SingleLeagueServiceProvider singleLeagueServiceprovider;
	protected final League league;

	public KnockoutGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;

		setWidth(320f, Unit.PIXELS);
		setMargin(true);

		addStyleName(ValoTheme.LAYOUT_CARD);
		addStyleName("bracket-game");

		HorizontalLayout wrapper = new HorizontalLayout();

		VerticalLayout teamWrapper = new VerticalLayout();
		teamWrapper.setMargin(false);
		teamWrapper.addComponent(createHomeTeamComponent(game));
		teamWrapper.addComponent(createAwayTeamComponent(game));

		VerticalLayout scoreWrapper = new VerticalLayout();
		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			GridUtil.getTextFieldScoreLayout(
					game,
					KnockoutGameBean::getHomeTeamScore,
					KnockoutGameBean::setHomeTeamScore,
					KnockoutGameBean::getAwayTeamScore,
					KnockoutGameBean::setAwayTeamScore,
					scoreChanged,
					scoreWrapper,
					false);
		} else {
			Label homeTeamScoreLabel = new Label(game.getHomeTeamScore() != null ? game.getHomeTeamScore().toString() : "");
			Label awayTeamScoreLabel = new Label(game.getAwayTeamScore() != null ? game.getAwayTeamScore().toString() : "");
			scoreWrapper.addComponent(homeTeamScoreLabel);
			scoreWrapper.addComponent(awayTeamScoreLabel);
			scoreWrapper.setComponentAlignment(homeTeamScoreLabel, Alignment.MIDDLE_CENTER);
			scoreWrapper.setComponentAlignment(awayTeamScoreLabel, Alignment.MIDDLE_CENTER);
		}
		scoreWrapper.setMargin(false);

		VerticalLayout winnerWrapper = new VerticalLayout();
		winnerWrapper.setMargin(false);
		winnerWrapper.setVisible(game.scoreNotNullAndEqual());
		RadioButtonGroup<String> winnerSelection = new RadioButtonGroup<>();
		winnerSelection.addStyleName("winner-selection");
		winnerSelection.setItems("homeTeam", "awayTeam");
		winnerSelection.setItemCaptionGenerator(ignored -> "");
		if (game.getHomeTeamIsWinner().isPresent()) {
			winnerSelection.setValue(game.getHomeTeamIsWinner().get() ? "homeTeam" : "awayTeam");
		}
		winnerSelection.addValueChangeListener(event -> {
			game.setHomeTeamIsWinner("homeTeam".equals(event.getValue()));
			scoreChanged.onNext(true);
		});
		winnerSelection.setEnabled(singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league));
		winnerWrapper.addComponent(winnerSelection);

		scoreChanged.subscribe(valid -> winnerWrapper.setVisible(valid && game.scoreNotNullAndEqual()));

		wrapper.addComponent(teamWrapper);
		wrapper.addComponent(scoreWrapper);
		wrapper.addComponent(winnerWrapper);

		String date = DateUtil.DATE_TIME_FORMATTER.format(game.getGame().getGame_date_time());
		Label infoLabel = new Label(game.getGame().getLocation() + " <br/> " + date, ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);
		infoLabel.setWidth(150f, Unit.PIXELS);

		addComponent(wrapper);

		if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			HorizontalLayout bottomWrapper = new HorizontalLayout();
			bottomWrapper.setMargin(false);

			CustomButton updateScores = new CustomButton(Resources.getMessage("updateScores"), VaadinIcons.CHECK_CIRCLE_O);
			updateScores.setEnabled(false);
			updateScores.addClickListener(ignored -> {
				Game gameModelItem = game.setScoresAndGetModelItem();
				if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
					singleLeagueServiceprovider.getGameService().updateKnockoutStageScore(gameModelItem);
					updateScores.setEnabled(false);
				} else {
					Notification.show(Resources.getMessage("adminRightsRevoked"));
				}
			});

			scoreChanged
					.map(isValid -> isValid && game.scoresAreValid())
					.subscribe(updateScores::setEnabled);

			bottomWrapper.addComponent(infoLabel);
			bottomWrapper.addComponent(updateScores);
			bottomWrapper.setComponentAlignment(infoLabel, Alignment.MIDDLE_LEFT);
			bottomWrapper.setComponentAlignment(updateScores, Alignment.MIDDLE_RIGHT);

			addComponent(bottomWrapper);
		} else {
			addComponent(infoLabel);
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
