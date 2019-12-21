package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabSheet;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.faq.FaqTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.GroupStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage.KnockoutStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings.LeagueSettingsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserScoresTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.reactivex.Observable;

public class SingleLeagueView extends VerticalLayout {

	private final League league;
	private final LeagueTopBar topBar;

	private LazyTabSheet tabSheet;

	public SingleLeagueView(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.league = league;

		setMargin(false);
		setSpacing(false);

		tabSheet = new LazyTabSheet();
		tabSheet.addLazyTab("overview", "Overview", () -> new UserScoresTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("teamWeightsTab", "Purchase stocks", () -> new TeamWeightsTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("groupStageTab", "Group Stage", () -> new GroupStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("knockoutStageTab", "Knockout Stage", () -> new KnockoutStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("fas", "FAQ", () -> new FaqTab(league, singleLeagueServiceprovider));
		if (singleLeagueServiceprovider.loggedInUserIsLeagueCreator(league)) {
			tabSheet.addLazyTab("usersTab", "Users", () -> new UsersTab(league, singleLeagueServiceprovider));
			tabSheet.addLazyTab("leagueSettingsTab", "League Settings", () -> new LeagueSettingsTab(league, singleLeagueServiceprovider));
		}

		topBar = new LeagueTopBar(league);
		topBar.imageUploadedAndResized().subscribe(byteArrayInputStream -> {
			league.setLeague_picture(byteArrayInputStream.readAllBytes());
			singleLeagueServiceprovider.getLeagueRepository().saveAndFlush(league);
		});

		add(topBar);
		add(tabSheet);
	}
	public Observable<ClickEvent<Button>> backToLeaguesView() {
		return topBar.backToLeagues();
	}

}
