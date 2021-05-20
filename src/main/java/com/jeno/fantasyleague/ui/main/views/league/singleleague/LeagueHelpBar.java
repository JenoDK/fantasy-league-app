package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LeagueHelpBar extends VerticalLayout {

	private final LeagueBean leagueBean;
	private BehaviorSubject<LeagueUser> doNotShow = BehaviorSubject.create();
	private BehaviorSubject<LeagueUser> skipHelp = BehaviorSubject.create();

	public LeagueHelpBar(LeagueBean leagueBean) {
		this.leagueBean = leagueBean;

		initLayout();
	}

	private void initLayout() {
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		setSizeFull();

		StatusLabel infoLabel = new StatusLabel();
		CustomButton skip = new CustomButton("Next tip", VaadinIcon.ARROW_RIGHT.create());
		CustomButton doNotShow = new CustomButton("Never show tips", VaadinIcon.ARROWS_CROSS.create());
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setPadding(false);
		buttonLayout.setMargin(false);
		buttonLayout.setWidthFull();
		buttonLayout.add(skip, doNotShow);
		buttonLayout.setAlignItems(Alignment.CENTER);
		buttonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
//		buttonLayout.setVerticalComponentAlignment(Alignment.START, skip);
//		buttonLayout.setVerticalComponentAlignment(Alignment.END, doNotShow);
		add(infoLabel, buttonLayout);
	}

	public Observable<LeagueUser> doNotShow() {
		return doNotShow;
	}

	public Observable<LeagueUser> skipHelp() {
		return skipHelp;
	}
}
