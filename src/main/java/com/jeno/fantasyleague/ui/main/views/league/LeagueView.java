package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueGridLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.SingleLeagueView;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUIScope
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
        layout.removeAllComponents();
        layout.setSpacing(false);
        layout.addStyleName("league-view");

        leagueGridLayout = new LeagueGridLayout(singleLeagueServiceProvider);
        leagueGridLayout.setSizeFull();
        leagueGridLayout.clickedLeague().subscribe(this::viewLeague);
        layout.addComponent(leagueGridLayout);
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
        layout.removeAllComponents();
        singleLeagieView = new SingleLeagueView(league, singleLeagueServiceProvider);
        singleLeagieView.backToLeaguesView().subscribe(ignored -> showLeagueGridLayout());
        layout.addComponent(singleLeagieView);
        // Get view panel and reset scroll
	    ((Panel) ((VerticalLayout) UI.getCurrent().getContent()).getComponent(1)).setScrollTop(0);
    }

    private void showLeagueGridLayout() {
        layout.removeAllComponents();
        layout.addComponent(leagueGridLayout);
    }
}
