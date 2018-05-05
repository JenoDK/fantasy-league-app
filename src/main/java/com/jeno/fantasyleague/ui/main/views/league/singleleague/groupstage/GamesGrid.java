package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.field.StringToPositiveConverter;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Collection;
import java.util.List;

public class GamesGrid extends Grid<GameBean> {

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueService;
	private final BehaviorSubject<Object> scoreChanged = BehaviorSubject.create();
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
			addColumn(game -> getScoreLayoutForAdmin(game), new ComponentRenderer())
					.setCaption("Score")
					.setStyleGenerator(item -> "v-align-center");
		} else {
			addColumn(game -> getScores(game.getHome_team_score(), game.getAway_team_score()))
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

	private HorizontalLayout getScoreLayoutForAdmin(GameBean game) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(createScoreField(game, GameBean::getHome_team_score, GameBean::setHomeTeamScore));
		layout.addComponent(new Label("-"));
		layout.addComponent(createScoreField(game, GameBean::getAway_team_score, GameBean::setAwayTeamScore));
		return layout;
	}

	private TextField createScoreField(GameBean game, ValueProvider<GameBean, Integer> getter, Setter<GameBean, Integer> setter) {
		TextField field = new TextField();
		field.setWidth(30, Unit.PIXELS);
		field.addStyleName("v-slot-ignore-error-indicator");
		Binder<GameBean> binder = new Binder<>(GameBean.class);
		binder.forField(field)
				.withConverter(new StringToPositiveConverter(0, "Must enter a positive number"))
				.bind(getter, setter);
		binder.setBean(game);
		binder.addValueChangeListener(ignored -> scoreChanged.onNext(ignored.getValue()));
		field.addStyleName(ValoTheme.TEXTFIELD_TINY);
		return field;
	}

	private String getScores(Integer teamAScore, Integer teamBScore) {
		return GameBean.getTeamScore(teamAScore) + " - " + GameBean.getTeamScore(teamBScore);
	}

	public Observable<Object> scoreChanged() {
		return scoreChanged;
	}

}
