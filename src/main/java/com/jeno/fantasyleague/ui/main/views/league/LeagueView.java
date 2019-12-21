package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueGridLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.SingleLeagueView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;

public class LeagueView {

    @Autowired
    private SingleLeagueServiceProvider singleLeagueServiceProvider;

    private VerticalLayout layout;
    private LeagueGridLayout leagueGridLayout;
    private SingleLeagueView singleLeagieView;

    @PostConstruct
    void init() {
        layout = new VerticalLayout();
	    layout.setId("league-view-id");
        layout.removeAll();
        layout.setSpacing(false);
        layout.addClassName("league-view");

        leagueGridLayout = new LeagueGridLayout(singleLeagueServiceProvider);
        leagueGridLayout.setSizeFull();
        leagueGridLayout.clickedLeague().subscribe(this::viewLeague);
        layout.add(leagueGridLayout);
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public void setLeagues(List<League> leagues) {
        leagueGridLayout.setLeagues(leagues);
    }

    public Observable<League> newLeague() {
        return leagueGridLayout.newLeague();
    }

    public Observable<League> clickedLeague() {
        return leagueGridLayout.clickedLeague();
    }

    public void addLeague(League league) {
        leagueGridLayout.addLeague(league);
    }

    private void viewLeague(League league) {
        layout.removeAll();
        singleLeagieView = new SingleLeagueView(league, singleLeagueServiceProvider);
        singleLeagieView.backToLeaguesView().subscribe(ignored -> showLeagueGridLayout());
        layout.add(singleLeagieView);
    }

    private void showLeagueGridLayout() {
        layout.removeAll();
        layout.add(leagueGridLayout);
    }
}
