package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
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

import java.util.List;
import java.util.stream.Collectors;

public class GroupLayout extends VerticalLayout {

	public GroupLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		setSpacing(false);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H3);

		GamesGrid gamesGrid = new GamesGrid(league, singleLeagueService);
		gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));

		Button saveScoreUpdatesButton = new Button("Update scores", VaadinIcons.USER_CHECK);
		saveScoreUpdatesButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		saveScoreUpdatesButton.addStyleName(ValoTheme.BUTTON_TINY);
		saveScoreUpdatesButton.setVisible(false);
		saveScoreUpdatesButton.addClickListener(ignored -> {
			List<Game> changedGames = gamesGrid.getItems().stream()
					.filter(GameBean::scoreChanged)
					.map(GameBean::setTeamScoresAndGetModelItem)
					.collect(Collectors.toList());
			if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
				singleLeagueService.getGameRepository().saveAll(changedGames);
				saveScoreUpdatesButton.setVisible(false);
			} else {
				Notification.show("Your admin rights have been revoked, please refresh the page");
			}
		});
		gamesGrid.scoreChanged()
				.map(ignored -> gamesGrid.getItems().stream().anyMatch(GameBean::scoreChanged))
				.subscribe(saveScoreUpdatesButton::setVisible);

		Button refreshButton = new Button("Refresh", VaadinIcons.REFRESH);
		refreshButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		refreshButton.addStyleName(ValoTheme.BUTTON_TINY);
		refreshButton.addClickListener(ignored -> {
			gamesGrid.setItems(getGameBeans(singleLeagueService, league, group));
			saveScoreUpdatesButton.setVisible(false);
		});

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(100, Unit.PERCENTAGE);
		titleLayout.setSpacing(true);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(saveScoreUpdatesButton);
		titleLayout.addComponent(refreshButton);
		titleLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setComponentAlignment(saveScoreUpdatesButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setExpandRatio(groupLabel, 8);
		titleLayout.setExpandRatio(refreshButton, 1);
		titleLayout.setExpandRatio(saveScoreUpdatesButton, 2);
		addComponent(titleLayout);
		addComponent(gamesGrid);
	}

	private List<GameBean> getGameBeans(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		return singleLeagueService.getGameRepository().findByLeagueAndJoinTeams(league, group).stream()
				.map(GameBean::new)
				.collect(Collectors.toList());
	}
}
