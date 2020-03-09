package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LeagueGrid extends Grid<LeagueBean> {

	private final BehaviorSubject<LeagueBean> clickedLeague = BehaviorSubject.create();
	private ListDataProvider<LeagueBean> leagueListDataProvider = new ListDataProvider<>(Lists.newArrayList());

	public LeagueGrid() {
		super();
		initGrid();
	}

	private void initGrid() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);
		addThemeNames("card-grid", "no-row-borders");
		getStyle().set("border", "none");

		addColumn(new ComponentRenderer<>(league -> new LeagueCard(league, clickedLeague)));
		setDataProvider(leagueListDataProvider);
	}

	public void setLeagues(List<LeagueBean> leagues) {
		leagueListDataProvider.getItems().clear();
		addItems(leagues);
	}

	public void addLeague(LeagueBean league) {
		addItems(List.of(league));
	}

	private void addItems(List<LeagueBean> leagues) {
		leagueListDataProvider.getItems().addAll(leagues);
		leagueListDataProvider.refreshAll();
	}

	public Observable<LeagueBean> clickedLeague() {
		return clickedLeague;
	}

}
