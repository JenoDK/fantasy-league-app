package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.ui.common.StyleModifier;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueForm;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.SingleLeagueView;
import com.jeno.fantasyleague.ui.main.views.notifications.NotificationGrid;
import com.jeno.fantasyleague.ui.main.views.notifications.NotificationModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@SpringComponent
@UIScope
public class LeagueView {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;
	private final LeagueForm leagueForm;
	private final NotificationModel notificationModel;
	private final BehaviorSubject<UserNotification> leagueAccepted = BehaviorSubject.create();

	private VerticalLayout layout;
	private LeagueGrid leagueGrid;
	private StatusLabel leagueInfoLabel;
	private SingleLeagueView singleLeagieView;

	@Autowired
	public LeagueView(SingleLeagueServiceProvider singleLeagueServiceProvider, NotificationModel notificationModel) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.notificationModel = notificationModel;
		this.leagueForm = new LeagueForm();
		this.leagueGrid = new LeagueGrid();
		this.leagueInfoLabel = new StatusLabel(true);
		this.leagueInfoLabel.setVisible(false);

		layout = new VerticalLayout();
		layout.setId("league-view-id");
		layout.removeAll();
		layout.addClassName("league-view");
		layout.setAlignItems(FlexComponent.Alignment.CENTER);

		constructLeagueGridLayout();
		leagueGrid.clickedLeague().subscribe(this::viewLeague);
	}

	private void constructLeagueGridLayout() {
		List<UserNotification> userNotifications = singleLeagueServiceProvider.getSecurityHolder().getUserNotifications();
		if (!userNotifications.isEmpty()) {
			NotificationGrid notificationGrid = new NotificationGrid(userNotifications, singleLeagueServiceProvider.getSecurityHolder(), notificationModel, leagueAccepted);
			addSection(Lists.newArrayList(notificationGrid), "Notifications", style -> {});
		}
		addSection(Lists.newArrayList(leagueInfoLabel, leagueGrid), "Your leagues", style -> {});
		addSection(Lists.newArrayList(leagueForm), "Create new league", style -> style
				.set("background-color", "var(--lumo-primary-color-10pct)")
				.set("border-radius", "var(--lumo-border-radius)")
				.set("--lumo-border-radius", "1em"));
	}

	private void addSection(List<Component> components, String sectionTitle, StyleModifier styleModifier) {
		VerticalLayout sectionLayout = new VerticalLayout();
		H4 sectionTitleLabel = new H4(sectionTitle);
		sectionTitleLabel.getStyle().set("margin", "var(--lumo-space-m)");
		sectionLayout.add(sectionTitleLabel);
		sectionLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.START, sectionTitleLabel);
		sectionLayout.setSpacing(false);
		sectionLayout.setPadding(false);
		sectionLayout.setMaxWidth("1200px");
		styleModifier.modify(sectionLayout.getStyle());

		components.forEach(sectionLayout::add);
		components.forEach(c -> sectionLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, c));

		layout.add(sectionLayout);
	}

	public VerticalLayout getLayout() {
		return layout;
	}

	public void setLeagues(List<LeagueBean> leagues) {
		if (!leagues.isEmpty()) {
			leagueInfoLabel.setVisible(false);
			leagueGrid.setLeagues(leagues);
		} else {
			leagueInfoLabel.setVisible(true);
			leagueInfoLabel.setInfoText("Your leagues will appear here, wait until you get invited to one and refresh the page or create your own league");
		}
	}

	public Observable<League> newLeague() {
		return leagueForm.validSubmit();
	}

	public Observable<UserNotification> leagueAccepted() {
		return leagueAccepted;
	}

	public void addLeague(LeagueBean league) {
		leagueForm.reset();
		leagueGrid.addLeague(league);
	}

	private void viewLeague(LeagueBean league) {
		layout.removeAll();
		singleLeagieView = new SingleLeagueView(league, singleLeagueServiceProvider);
		singleLeagieView.backToLeaguesView().subscribe(ignored -> showLeagueGridLayout());
		layout.add(singleLeagieView);
	}

	protected void showLeagueGridLayout() {
		layout.removeAll();
		constructLeagueGridLayout();
		leagueGrid.getDataProvider().refreshAll();
	}
}
