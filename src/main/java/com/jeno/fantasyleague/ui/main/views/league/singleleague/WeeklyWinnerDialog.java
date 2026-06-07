package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.WeeklyWinner;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.stream.Collectors;

public class WeeklyWinnerDialog {

	private final WeeklyWinner weeklyWinner;

	public WeeklyWinnerDialog(WeeklyWinner weeklyWinner) {
		this.weeklyWinner = weeklyWinner;
	}

	public void show() {
		new PopupWindow.Builder(Resources.getMessage("weeklyWinnerTitle"), this::buildContent)
				.setType(PopupWindow.Type.ALERT)
				.setWidth("400px")
				.heightUndefined(true)
				.addExtraThemeNames("weekly-winner")
				.build()
				.open();
	}

	private Component buildContent(PopupWindow popupWindow) {
		VerticalLayout layout = new VerticalLayout();
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.setPadding(true);
		layout.setSpacing(true);

		layout.add(VaadinIcon.TROPHY.create());
		layout.add(new H3(Resources.getMessage("weeklyWinnerTitle")));

		String names = weeklyWinner.getWinners().stream()
				.map(User::getUsername)
				.sorted()
				.collect(Collectors.joining(", "));
		String score = OverviewUtil.getScoreFormatted(weeklyWinner.getTopScore()).toString();
		String messageKey = weeklyWinner.getWinners().size() == 1 ? "weeklyWinnerSingle" : "weeklyWinnerJoint";

		Span message = new Span(Resources.getMessage(messageKey, names, score));
		message.getStyle().set("text-align", "center");
		layout.add(message);

		return layout;
	}

}
