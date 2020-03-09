package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;

public class SingleLeagueView extends VerticalLayout {

	private final League league;

	private LeagueMenuBar tabSheet;

	public SingleLeagueView(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.league = league;

		initLayout(league, singleLeagueServiceprovider);
	}

	private void initLayout(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		setSizeFull();
		setMaxWidth("1200px");

		HorizontalLayout navigation = new HorizontalLayout();
		navigation.setWidthFull();

		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);
		main.setSizeFull();

		LeagueTopBar topBar = new LeagueTopBar(league);
		topBar.imageUploaded().subscribe(os -> {
			league.setLeague_picture(os.toByteArray());
			singleLeagueServiceprovider.getLeagueRepository().saveAndFlush(league);
		});


		tabSheet = new LeagueMenuBar(league, singleLeagueServiceprovider, main);
		navigation.add(tabSheet);

		add(topBar);
//		add(createNavigationRow("Matches", VaadinIcon.SWORD.create()));
		add(navigation);
		add(main);
	}

	private Component createNavigationRow(String title, Icon icon) {
		CustomButton button = new CustomButton(title, icon);
		button.setSizeFull();
		button.setHeight("100px");
		button.addThemeName("navigation-row");
		return button;
	}

	public Observable<ClickEvent<MenuItem>> backToLeaguesView() {
		return tabSheet.backToLeaguesView();
	}

}
