package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueGridLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.SingleLeagueView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.reactivex.Observable;

@SpringComponent
@UIScope
public class LeagueView {

    private final SingleLeagueServiceProvider singleLeagueServiceProvider;

    private VerticalLayout layout;
    private LeagueGridLayout leagueGridLayout;
    private SingleLeagueView singleLeagieView;

    @Autowired
    public LeagueView(SingleLeagueServiceProvider singleLeagueServiceProvider) {
        layout = new VerticalLayout();
	    layout.setId("league-view-id");
        layout.removeAll();
        layout.setSpacing(false);
        layout.addClassName("league-view");

        leagueGridLayout = new LeagueGridLayout(singleLeagueServiceProvider);
        leagueGridLayout.setSizeFull();
        leagueGridLayout.clickedLeague().subscribe(this::viewLeague);
        layout.add(leagueGridLayout);
        this.singleLeagueServiceProvider = singleLeagueServiceProvider;
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
