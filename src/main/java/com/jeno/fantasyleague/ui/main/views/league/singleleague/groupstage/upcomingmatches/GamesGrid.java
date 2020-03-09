package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class GamesGrid extends Grid<GameBean> {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueService;
	private final BehaviorSubject<GameBean> scoreChanged = BehaviorSubject.create();
	private final BehaviorSubject<GameBean> predictionChanged = BehaviorSubject.create();
	private List<GameBean> items;

	public GamesGrid(League league, SingleLeagueServiceProvider singleLeagueService) {
		super();
		this.league = league;
		this.singleLeagueService = singleLeagueService;

		setWidth("100%");
		setHeightByRows(true);

		initGrid();
	}

	@Override
	public void setItems(Collection<GameBean> items) {
		super.setItems(items);
		this.items = Lists.newArrayList(items);
		setHeight((38f * (items.size() + 1)) + "px");
	}

	public List<GameBean> getItems() {
		return items;
	}

	private void initGrid() {
		addColumn(new ComponentRenderer<>(game -> LayoutUtil.createTeamLayout(game.getHome_team())))
			.setHeader("Team A");
		if (singleLeagueService.loggedInUserIsLeagueAdmin(league)) {
			addColumn(new ComponentRenderer<>(game -> LayoutUtil.getTextFieldScoreLayout(
					game,
					GameBean::getHome_team_score,
					GameBean::setHomeTeamScore,
					GameBean::getAway_team_score,
					GameBean::setAwayTeamScore,
					scoreChanged,
					new HorizontalLayout())))
				.setHeader("Score")
				.setClassNameGenerator(item -> "v-align-center");
		} else {
			addColumn(game -> LayoutUtil.getScores(game.getHome_team_score(), game.getAway_team_score()))
				.setHeader("Score")
				.setClassNameGenerator(item -> "v-align-center");
		}
		addColumn(new ComponentRenderer<>(game -> LayoutUtil.createTeamLayout(game.getAway_team())))
			.setHeader("Team B");
		addColumn(new ComponentRenderer<>(this::getGameInfo))
			.setHeader("Info");
		addColumn(new ComponentRenderer<>(this::getPredictionLayout))
			.setHeader("Prediction")
			.setClassNameGenerator(item -> "v-align-center");
	}

	private Label getGameInfo(GameBean gameBean) {
		String date = DateUtil.DATE_TIME_FORMATTER.format(gameBean.getGame_date_time());
		Label infoLabel = new Label("<b>Round " + gameBean.getRound() + "</b> " + gameBean.getLocation() + " <br/> " + date);
//		infoLabel.addClassName(ValoTheme.LABEL_TINY);
		return infoLabel;
	}

	private HorizontalLayout getPredictionLayout(GameBean gameBean) {
		if (LocalDateTime.now().isBefore(league.getLeague_starting_date())) {
			return LayoutUtil.getTextFieldScoreLayout(
					gameBean,
					GameBean::getHomeTeamPrediction,
					GameBean::setHomeTeamPrediction,
					GameBean::getAwayTeamPrediction,
					GameBean::setAwayTeamPrediction,
					predictionChanged,
					new HorizontalLayout());
		} else {
			HorizontalLayout layout = new HorizontalLayout();
			layout.add(new Label(LayoutUtil.getScores(gameBean.getHomeTeamPrediction(), gameBean.getAwayTeamPrediction())));
			return layout;
		}
	}

	public Observable<GameBean> scoreChanged() {
		return scoreChanged;
	}

	public Observable<GameBean> predictionChanged() {
		return predictionChanged;
	}

}
