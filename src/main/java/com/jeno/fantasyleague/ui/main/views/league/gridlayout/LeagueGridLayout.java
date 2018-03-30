package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.model.League;
import com.vaadin.ui.GridLayout;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeagueGridLayout extends GridLayout {

	private final BehaviorSubject<League> clickedLeague = BehaviorSubject.create();

	private NewLeagueGridComponent newLeagueComponent;
	private Map<Long, ExistingLeagueGridComponent> leagueMap = Maps.newHashMap();

	public LeagueGridLayout() {
		super(3, 1);
		setSpacing(true);
		addStyleName("league-grid-layout");

		newLeagueComponent = new NewLeagueGridComponent();
		addComponent(newLeagueComponent);
	}

	public void setLeagues(List<League> leagues) {
		removeAllComponents();
		addComponent(newLeagueComponent);

		leagueMap.putAll(leagues.stream()
			.collect(Collectors.toMap(League::getId, ExistingLeagueGridComponent::new)));
		leagueMap.values().forEach(this::addLeagueComponent);
	}

	public void addLeague(League league) {
		newLeagueComponent.reset();
		if (!leagueMap.containsKey(league.getId())) {
			ExistingLeagueGridComponent value = new ExistingLeagueGridComponent(league);
			leagueMap.put(league.getId(), value);
			addLeagueComponent(value);
		}
	}

	private void addLeagueComponent(ExistingLeagueGridComponent value) {
		value.click().subscribe(clickedLeague::onNext);
		addComponent(value);
	}

	public Observable<LeagueBean> newLeague() {
		return newLeagueComponent.newLeague();
	}

	public Observable<League> clickedLeague() {
		return clickedLeague;
	}

}
