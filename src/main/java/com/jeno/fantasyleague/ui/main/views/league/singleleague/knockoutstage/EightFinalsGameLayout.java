package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Initializer;
import com.jeno.fantasyleague.data.service.repo.game.GameServiceImpl;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class EightFinalsGameLayout extends KnockoutGameLayout {

	public EightFinalsGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super(singleLeagueServiceprovider, league, game);
	}

	@Override
	protected HorizontalLayout createHomeTeamComponent(KnockoutGameBean game) {
		return createTeamComponent(
				game,
				game.getGame().getHome_team_placeholder(),
				game.getHomeTeam(),
				contestant -> game.setHomeTeam(contestant));
	}

	@Override
	protected HorizontalLayout createAwayTeamComponent(KnockoutGameBean game) {
		return createTeamComponent(
				game, game.getGame().getAway_team_placeholder(),
				game.getAwayTeam(),
				contestant -> game.setAwayTeam(contestant));
	}

	private HorizontalLayout createTeamComponent(
			KnockoutGameBean game,
			String placeHolder,
			Contestant possibleContestant,
			Consumer<Contestant> contestantConsumer) {
		if (LocalDateTime.now().isBefore(game.getGame().getGame_date_time()) &&
				singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			ComboBox<Contestant> contestantCombobox = getContestantComboBox(game, placeHolder, possibleContestant, contestantConsumer);
			return new HorizontalLayout(contestantCombobox);
		} else {
			return GridUtil.createTeamLayout(possibleContestant, placeHolder);
		}
	}

	private ComboBox<Contestant> getContestantComboBox(KnockoutGameBean game, String placeHolder, Contestant possibleContestant, Consumer<Contestant> contestantConsumer) {
		DataProvider<Contestant, String> dataProvider = getDataProvider(GameServiceImpl.getGroup(placeHolder).get());
		ComboBox<Contestant> contestantCombobox = new ComboBox<>();
		contestantCombobox.addStyleName("contestantSelection");
		contestantCombobox.addStyleName(ValoTheme.COMBOBOX_SMALL);
		contestantCombobox.setPlaceholder(placeHolder);
		contestantCombobox.setTextInputAllowed(false);
		contestantCombobox.addFocusListener(ignored -> dataProvider.refreshAll());
		contestantCombobox.setDataProvider(dataProvider);
		contestantCombobox.setItemCaptionGenerator(Contestant::getName);
		contestantCombobox.setItemIconGenerator(contestant -> new ThemeResource(contestant.getIcon_path()));
		contestantCombobox.addValueChangeListener(event -> {
			if (singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
				contestantConsumer.accept(event.getValue());
				Game updatedGame = singleLeagueServiceprovider.getGameRepository().saveAndFlush(game.getGame());
				dataProvider.refreshAll();
				if (updatedGame.getHome_team() != null && updatedGame.getAway_team() != null && scoreWrapper != null) {
					scoreWrapper.setEnabled(true);
					predictionWrapper.setEnabled(true);
				}
			} else {
				Notification.show(Resources.getMessage("adminRightsRevoked"), Notification.Type.WARNING_MESSAGE);
			}
		});
		if (possibleContestant != null) {
			contestantCombobox.setValue(possibleContestant);
		}
		return contestantCombobox;
	}

	private DataProvider<Contestant, String> getDataProvider(FifaWorldCup2018Initializer.Group group) {
		return new CallbackDataProvider<>(
				q -> singleLeagueServiceprovider.getContestantService().getPossibleContestantsFromGroupStage(group, league).stream(),
				q -> singleLeagueServiceprovider.getContestantService().getPossibleContestantsFromGroupStage(group, league).size());
	}

}
