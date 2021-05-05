package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MatchGrid extends Grid<MatchBean> {

	private final BehaviorSubject<MatchBean> clickedMatch = BehaviorSubject.create();
	private final BehaviorSubject<MatchPredictionBean> predictionChanged = BehaviorSubject.create();
	private final BehaviorSubject<MatchPredictionBean> scoreChanged = BehaviorSubject.create();
	private final boolean loggedInUserIsAdmin;
	private SingleLeagueServiceProvider singleLeagueServiceProvider;

	private ListDataProvider<MatchBean> matchListDataProvider = new ListDataProvider<>(Lists.newArrayList());

	public MatchGrid(SingleLeagueServiceProvider singleLeagueServiceProvider, boolean loggedInUserIsAdmin) {
		super();
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.loggedInUserIsAdmin = loggedInUserIsAdmin;
		initGrid();
	}

	private void initGrid() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);

		addColumn(new ComponentRenderer<>(match -> {
			MatchCardLayout card = new MatchCardLayout(match, clickedMatch, loggedInUserIsAdmin, true, singleLeagueServiceProvider);
			card.predictionChanged().subscribe(predictionChanged::onNext);
			card.scoreChanged().subscribe(scoreChanged::onNext);
			return card;
		}));
		addThemeNames("card-grid", "no-row-borders");
		getStyle().set("border", "none");
		setDataProvider(matchListDataProvider);
	}

	public void filterOnStage(SoccerCupStages stage) {
		matchListDataProvider.setFilter(matchBean -> stage.toString().equals(matchBean.getGame().getStage()));
	}

	public void clearFilter() {
		matchListDataProvider.clearFilters();
	}

	public void setMatches(List<MatchBean> matches) {
		matchListDataProvider.getItems().clear();
		addItems(matches);
	}

	private void addItems(List<MatchBean> matches) {
		matchListDataProvider.getItems().addAll(matches);
		matchListDataProvider.refreshAll();
	}

	public Observable<MatchBean> clickedMatch() {
		return clickedMatch;
	}

	public Observable<MatchPredictionBean> predictionChanged() {
		return predictionChanged;
	}

	public Observable<MatchPredictionBean> scoreChanged() {
		return scoreChanged;
	}

}