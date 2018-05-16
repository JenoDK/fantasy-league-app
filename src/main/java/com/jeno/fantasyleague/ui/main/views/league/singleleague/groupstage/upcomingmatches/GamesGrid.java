package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Collection;
import java.util.List;

public class GamesGrid extends Grid<GameBean> {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueService;
	private final BehaviorSubject<Boolean> scoreChanged = BehaviorSubject.create();
	private List<GameBean> items;

	public GamesGrid(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		this.league = league;
		this.singleLeagueService = singleLeagueService;

		setWidth("100%");
		setHeightMode(HeightMode.ROW);

		initGrid();
	}

	@Override
	public void setItems(Collection<GameBean> items) {
		super.setItems(items);
		this.items = Lists.newArrayList(items);
		setHeight(38f * (items.size() + 1), Unit.PIXELS);
	}

	public List<GameBean> getItems() {
		return items;
	}

	private void initGrid() {
		addColumn(game -> GridUtil.createTeamLayout(game.getHome_team()), new ComponentRenderer())
			.setCaption("Team A");
		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			addColumn(game -> GridUtil.getTextFieldScoreLayout(
					game,
					GameBean::getHome_team_score,
					GameBean::setHomeTeamScore,
					GameBean::getAway_team_score,
					GameBean::setAwayTeamScore,
					scoreChanged), new ComponentRenderer())
				.setCaption("Score")
				.setStyleGenerator(item -> "v-align-center");
		} else {
			addColumn(game -> GridUtil.getScores(game.getHome_team_score(), game.getAway_team_score()))
				.setCaption("Score")
				.setStyleGenerator(item -> "v-align-center");
		}
		addColumn(game -> GridUtil.createTeamLayout(game.getAway_team()), new ComponentRenderer())
			.setCaption("Team B");
		addColumn(GameBean::getRound)
			.setCaption("Round");
		addColumn(GameBean::getLocation)
			.setCaption("Location");
		addColumn(GameBean::getGame_date_time, new LocalDateTimeRenderer())
			.setCaption("Date");
	}

	public Observable<Boolean> scoreChanged() {
		return scoreChanged;
	}

}
