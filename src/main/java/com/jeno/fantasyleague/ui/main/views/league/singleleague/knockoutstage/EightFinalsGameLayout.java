package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Initializer;
import com.jeno.fantasyleague.backend.data.service.repo.game.GameServiceImpl;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

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
		if (LocalDateTime.now().isBefore(game.getGame().getGameDateTime()) &&
				singleLeagueServiceprovider.loggedInUserIsLeagueAdmin(league)) {
			ComboBox<Contestant> contestantCombobox = getContestantComboBox(game, placeHolder, possibleContestant, contestantConsumer);
			return new HorizontalLayout(contestantCombobox);
		} else {
			return LayoutUtil.createTeamLayout(possibleContestant, placeHolder);
		}
	}

	private ComboBox<Contestant> getContestantComboBox(KnockoutGameBean game, String placeHolder, Contestant possibleContestant, Consumer<Contestant> contestantConsumer) {
		DataProvider<Contestant, String> dataProvider = getDataProvider(GameServiceImpl.getGroup(placeHolder).get());
		ComboBox<Contestant> contestantCombobox = new ComboBox<>();
		contestantCombobox.addClassName("contestantSelection");
//		contestantCombobox.addClassName(ValoTheme.COMBOBOX_SMALL);
		contestantCombobox.setPlaceholder(placeHolder);
//		contestantCombobox.setTextInputAllowed(false);
		contestantCombobox.addFocusListener(ignored -> dataProvider.refreshAll());
		contestantCombobox.setDataProvider(dataProvider);
		contestantCombobox.setItemLabelGenerator(Contestant::getName);
//		contestantCombobox.setItemIconGenerator(contestant -> new ThemeResource(contestant.getIcon_path()));
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
				Notification.show(Resources.getMessage("adminRightsRevoked"));
			}
		});
		if (possibleContestant != null) {
			contestantCombobox.setValue(possibleContestant);
		}
		return contestantCombobox;
	}

	private DataProvider<Contestant, String> getDataProvider(UefaEuro2020Initializer.Group group) {
		return new CallbackDataProvider<>(
				q -> singleLeagueServiceprovider.getContestantService().getPossibleContestantsFromGroupStage(group, league).stream(),
				q -> singleLeagueServiceprovider.getContestantService().getPossibleContestantsFromGroupStage(group, league).size());
	}

}
