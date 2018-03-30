package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GamesGrid extends Grid<Game> {

	private Set<Game> games;

	public GamesGrid(Set<Game> games) {
		this.games = games;

		setWidth("100%");
		setHeight(38f * (games.size() + 1), Unit.PIXELS);
		setSelectionMode(SelectionMode.NONE);

		initGrid();
	}

	private void initGrid() {
		setItems(games.stream()
			.sorted(Comparator.comparing(Game::getGame_date_time))
			.collect(Collectors.toList()));
		addColumn(game -> createTeamLayout(game.getHome_team()), new ComponentRenderer())
			.setCaption("Home team");
		addColumn(game -> createTeamLayout(game.getAway_team()), new ComponentRenderer())
			.setCaption("Away team");
		addColumn(Game::getLocation)
			.setCaption("Location");
		addColumn(Game::getGame_date_time, new LocalDateTimeRenderer())
			.setCaption("Date");
	}

	private HorizontalLayout createTeamLayout(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth(30f, Unit.PIXELS);
		icon.setHeight(30f, Unit.PIXELS);
		icon.setSource(new ThemeResource(contestant.getIcon_path()));
		Label teamName = new Label(contestant.getName());
		layout.addComponents(icon, teamName);
		return layout;
	}

}
