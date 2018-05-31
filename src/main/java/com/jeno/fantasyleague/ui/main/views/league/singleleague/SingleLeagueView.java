package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import java.nio.file.Files;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabSheet;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.faq.FaqTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.GroupStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage.KnockoutStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings.LeagueSettingsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.UserScoresTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
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
		tabSheet.addLazyTab("groupStageTab", "Group Stage", () -> new GroupStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("knockoutStageTab", "Knockout Stage", () -> new KnockoutStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("teamWeightsTab", "Purchase stocks", () -> new TeamWeightsTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("overview", "Overview", () -> new UserScoresTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("fas", "FAQ", () -> new FaqTab(league, singleLeagueServiceprovider));
		if (singleLeagueServiceprovider.loggedInUserIsLeagueCreator(league)) {
			tabSheet.addLazyTab("usersTab", "Users", () -> new UsersTab(league, singleLeagueServiceprovider));
			tabSheet.addLazyTab("leagueSettingsTab", "League Settings", () -> new LeagueSettingsTab(league, singleLeagueServiceprovider));
		}

		topBar = new LeagueTopBar(league);
		topBar.imageUploadedAndResized().subscribe(file -> {
			league.setLeague_picture(Files.readAllBytes(file.toPath()));
			singleLeagueServiceprovider.getLeagueRepository().saveAndFlush(league);
		});

		addComponent(topBar);
		addComponent(tabSheet);
	}
	public Observable<Button.ClickEvent> backToLeaguesView() {
		return topBar.backToLEagues();
	}

}
