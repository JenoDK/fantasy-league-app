package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.annotation.SpringUIScope;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueGridLayout;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.SingleLeagueView;
import com.vaadin.ui.VerticalLayout;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringUIScope
public class LeagueView {

    @Autowired
    private SecurityHolder securityHolder;

    private VerticalLayout layout;
    private LeagueGridLayout leagueGridLayout;
    private SingleLeagueView singleLeagieView;

    @PostConstruct
    void init() {
        layout = new VerticalLayout();
        layout.removeAllComponents();
        layout.setSpacing(false);
        layout.addStyleName("league-view");

        leagueGridLayout = new LeagueGridLayout();
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

    public Observable<LeagueBean> newLeague() {
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
        singleLeagieView = new SingleLeagueView(league, securityHolder.getUser());
        singleLeagieView.backToLeaguesView().subscribe(ignored -> showLeagueGridLayout());
        layout.addComponent(singleLeagieView);
    }

    private void showLeagueGridLayout() {
        layout.removeAllComponents();
        layout.addComponent(leagueGridLayout);
    }
}
