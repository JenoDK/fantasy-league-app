package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabSheet;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.GroupStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage.KnockoutStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings.LeagueSettingsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightsTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.userscores.UserScoresTab;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class SingleLeagueView extends VerticalLayout {

	private final League league;

	private Button backToLeaguesView;
	private Label title;
	private LazyTabSheet tabSheet;

	public SingleLeagueView(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.league = league;

		setMargin(false);
		setSpacing(false);

		backToLeaguesView = new Button("Leagues", VaadinIcons.ARROW_CIRCLE_LEFT);

		title = new Label(league.getName(), ContentMode.HTML);
		title.addStyleName(ValoTheme.LABEL_H1);

		tabSheet = new LazyTabSheet();
		tabSheet.addLazyTab("groupStageTab", "Group Stage", () -> new GroupStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("knockoutStageTab", "Knockout Stage", () -> new KnockoutStageTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("usersTab", "Users", () -> new UsersTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("teamWeightsTab", "My Team Weights", () -> new TeamWeightsTab(league, singleLeagueServiceprovider));
		tabSheet.addLazyTab("usersScoreTab", "User Scores", () -> new UserScoresTab(league, singleLeagueServiceprovider));
		if (singleLeagueServiceprovider.loggedInUserIsLeagueCreator(league)) {
			tabSheet.addLazyTab("leagueSettingsTab", "League Settings", () -> new LeagueSettingsTab(league, singleLeagueServiceprovider));
		}

		addComponent(backToLeaguesView);
		addComponent(title);
		addComponent(tabSheet);
	}
	public Observable<Button.ClickEvent> backToLeaguesView() {
		return RxUtil.clicks(backToLeaguesView);
	}

}
