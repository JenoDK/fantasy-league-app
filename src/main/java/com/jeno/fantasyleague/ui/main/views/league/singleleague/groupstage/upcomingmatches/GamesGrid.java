package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.themes.ValoTheme;
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
					scoreChanged,
					new HorizontalLayout()), new ComponentRenderer())
				.setCaption("Score")
				.setStyleGenerator(item -> "v-align-center");
		} else {
			addColumn(game -> GridUtil.getScores(game.getHome_team_score(), game.getAway_team_score()))
				.setCaption("Score")
				.setStyleGenerator(item -> "v-align-center");
		}
		addColumn(game -> GridUtil.createTeamLayout(game.getAway_team()), new ComponentRenderer())
			.setCaption("Team B");
		addColumn(this::getGameInfo, new ComponentRenderer())
			.setCaption("Info");
		addColumn(game -> getPredictionLayout(game), new ComponentRenderer())
			.setCaption("Prediction")
			.setStyleGenerator(item -> "v-align-center");
	}

	private Label getGameInfo(GameBean gameBean) {
		String date = DateUtil.DATE_TIME_FORMATTER.format(gameBean.getGame_date_time());
		Label infoLabel = new Label("<b>Round " + gameBean.getRound() + "</b> " + gameBean.getLocation() + " <br/> " + date , ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);
		return infoLabel;
	}

	private HorizontalLayout getPredictionLayout(GameBean gameBean) {
		if (LocalDateTime.now().isBefore(league.getLeague_starting_date())) {
			return (HorizontalLayout) GridUtil.getTextFieldScoreLayout(
					gameBean,
					GameBean::getHomeTeamPrediction,
					GameBean::setHomeTeamPrediction,
					GameBean::getAwayTeamPrediction,
					GameBean::setAwayTeamPrediction,
					predictionChanged,
					new HorizontalLayout());
		} else {
			HorizontalLayout layout = new HorizontalLayout();
			layout.addComponent(new Label(GridUtil.getScores(gameBean.getHomeTeamPrediction(), gameBean.getAwayTeamPrediction())));
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
