package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GroupLayout extends VerticalLayout {


	public GroupLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		setSpacing(false);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H3);

		GamesGrid gamesGrid = new GamesGrid(league, singleLeagueService);
		gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));

		Button refreshButton = new CustomButton("Refresh", VaadinIcons.REFRESH);
		refreshButton.addClickListener(ignored -> {
			gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));
		});

		HorizontalLayout titleLayout;

		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			titleLayout = createAdminTitleLayout(singleLeagueService, league, group, groupLabel, gamesGrid, refreshButton);
		} else {
			titleLayout = createUserTitleLayout(groupLabel, refreshButton);
		}

		addComponent(titleLayout);
		addComponent(gamesGrid);
	}

	private HorizontalLayout createUserTitleLayout(Label groupLabel, Button refreshButton) {
		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(100, Unit.PERCENTAGE);
		titleLayout.setSpacing(true);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(refreshButton);
		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setExpandRatio(groupLabel, 8);
		titleLayout.setExpandRatio(refreshButton, 2);
		return titleLayout;
	}

	public HorizontalLayout createAdminTitleLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group, Label groupLabel, GamesGrid gamesGrid, Button refreshButton) {
		Button saveScoreUpdatesButton = new CustomButton(Resources.getMessage("updateScores"), VaadinIcons.CHECK_CIRCLE_O);
		saveScoreUpdatesButton.setEnabled(false);
		saveScoreUpdatesButton.addClickListener(ignored -> {
			List<Game> changedGames = gamesGrid.getItems().stream()
					.filter(GameBean::scoreChangedAndIsValid)
					.map(GameBean::setTeamScoresAndGetModelItem)
					.collect(Collectors.toList());
			saveGameScores(singleLeagueService, league, saveScoreUpdatesButton, changedGames);
		});

		gamesGrid.scoreChanged()
				.map(isValid -> gamesGrid.getItems().stream().anyMatch(GameBean::scoreChangedAndIsValid) && isValid)
				.subscribe(saveScoreUpdatesButton::setEnabled);

		refreshButton.addClickListener(ignored -> saveScoreUpdatesButton.setEnabled(false));

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(100, Unit.PERCENTAGE);
		titleLayout.setSpacing(true);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(saveScoreUpdatesButton);
		titleLayout.addComponent(refreshButton);
		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setComponentAlignment(saveScoreUpdatesButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setExpandRatio(groupLabel, 8);
		titleLayout.setExpandRatio(refreshButton, 2);
		titleLayout.setExpandRatio(saveScoreUpdatesButton, 2);
		return titleLayout;
	}

	public void saveGameScores(SingleLeagueServiceProvider singleLeagueService, League league, Button saveScoreUpdatesButton, List<Game> changedGames) {
		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			singleLeagueService.getGameService().updateGroupStageGameScores(changedGames);
			saveScoreUpdatesButton.setEnabled(false);
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"));
		}
	}

	private List<GameBean> getGameBeans(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		return singleLeagueService.getGameRepository().findByLeagueAndGroupStageAndJoinTeams(league, group).stream()
				.map(GameBean::new)
				.collect(Collectors.toList());
	}
}
