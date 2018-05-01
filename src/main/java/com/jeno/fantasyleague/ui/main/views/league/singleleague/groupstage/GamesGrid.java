package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

public class GamesGrid extends Grid<Game> {

	public GamesGrid(ListDataProvider<Game> dataProvider) {
		super();
		setDataProvider(dataProvider);

		setWidth("100%");
		setHeightMode(HeightMode.ROW);
		setHeight(38f * (dataProvider.getItems().size() + 1), Unit.PIXELS);

		initGrid();
	}

	private void initGrid() {
		addColumn(game -> createTeamLayout(game.getHome_team()), new ComponentRenderer())
			.setCaption("Home team");
		addColumn(game -> getScore(game.getHome_team_score()))
				.setCaption("Home team score");
		addColumn(game -> createTeamLayout(game.getAway_team()), new ComponentRenderer())
			.setCaption("Away team");
		addColumn(game -> getScore(game.getAway_team_score()))
				.setCaption("Away team score");
		addColumn(Game::getRound)
				.setCaption("Round");
		addColumn(Game::getLocation)
			.setCaption("Location");
		addColumn(Game::getGame_date_time, new LocalDateTimeRenderer())
			.setCaption("Date");
	}

	private String getScore(Integer score) {
		return score != null ? score.toString() : "-";
	}

	private HorizontalLayout createTeamLayout(Contestant contestant) {
		HorizontalLayout layout = new HorizontalLayout();
		Image icon = new Image();
		icon.setWidth(42f, Unit.PIXELS);
		icon.setHeight(28f, Unit.PIXELS);
		icon.setSource(new ThemeResource(contestant.getIcon_path()));
		Label teamName = new Label(contestant.getName());
		layout.addComponents(icon, teamName);
		return layout;
	}

}
