package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.GroupStageTab;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.users.UsersTab;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

public class SingleLeagueView extends VerticalLayout {

	private final League league;
	private final User user;

	private Button backToLeaguesView;
	private Label title;
	private TabSheet tabSheet;

	public SingleLeagueView(League league, SingleLeagueServiceProvider singleLeagueServiceprovider, User user) {
		super();
		this.league = league;
		this.user = user;

		setMargin(false);
		setSpacing(false);

		backToLeaguesView = new Button("Leagues", VaadinIcons.ARROW_CIRCLE_LEFT);

		title = new Label(league.getName(), ContentMode.HTML);
		title.addStyleName(ValoTheme.LABEL_H1);

		tabSheet = new TabSheet();
		tabSheet.addTab(new GroupStageTab(league, singleLeagueServiceprovider), "Group stage");
		tabSheet.addTab(new UsersTab(league, singleLeagueServiceprovider), "Users");

		addComponent(backToLeaguesView);
		addComponent(title);
		addComponent(tabSheet);
	}
	public Observable<Button.ClickEvent> backToLeaguesView() {
		return RxUtil.clicks(backToLeaguesView);
	}

}
