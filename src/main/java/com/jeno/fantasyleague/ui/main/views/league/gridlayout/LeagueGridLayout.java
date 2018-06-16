package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.ui.GridLayout;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LeagueGridLayout extends GridLayout {

	private final BehaviorSubject<League> clickedLeague = BehaviorSubject.create();

	private SingleLeagueServiceProvider singleLeagueServiceProvider;
	private NewLeagueGridComponent newLeagueComponent;
	private Map<Long, ExistingLeagueGridComponent> leagueMap = Maps.newHashMap();

	public LeagueGridLayout(SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super(1, 1);
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		setSpacing(true);
		addStyleName("league-grid-layout");

		newLeagueComponent = new NewLeagueGridComponent();
		addComponent(newLeagueComponent);
	}

	public void setLeagues(List<League> leagues) {
		removeAllComponents();
		addComponent(newLeagueComponent);

		leagueMap.putAll(leagues.stream()
			.collect(Collectors.toMap(League::getId, league -> new ExistingLeagueGridComponent(league, singleLeagueServiceProvider))));
		leagueMap.values().forEach(this::addLeagueComponent);
	}

	public void addLeague(League league) {
		newLeagueComponent.reset();
		if (!leagueMap.containsKey(league.getId())) {
			ExistingLeagueGridComponent value = new ExistingLeagueGridComponent(league, singleLeagueServiceProvider);
			leagueMap.put(league.getId(), value);
			addLeagueComponent(value);
		}
	}

	private void addLeagueComponent(ExistingLeagueGridComponent value) {
		value.click().subscribe(clickedLeague::onNext);
		addComponent(value);
	}

	public Observable<League> newLeague() {
		return newLeagueComponent.newLeague();
	}

	public Observable<League> clickedLeague() {
		return clickedLeague;
	}

}
