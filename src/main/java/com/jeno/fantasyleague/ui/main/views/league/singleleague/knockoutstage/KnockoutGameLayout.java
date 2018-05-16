package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class KnockoutGameLayout extends VerticalLayout {

	public KnockoutGameLayout(KnockoutGameBean game) {
		super();

		setWidth(230f, Unit.PIXELS);
		setMargin(true);

		addStyleName(ValoTheme.LAYOUT_CARD);
		addStyleName("bracket-game");

		Component homeTeamComp = createTeamComponent(game.getContestant1(), game.getGame().getHome_team_score());
		Component awayTeamComp = createTeamComponent(game.getContestant2(), game.getGame().getAway_team_score());

		String date = DateUtil.DATE_TIME_FORMATTER.format(game.getGame().getGame_date_time());
		Label infoLabel = new Label(game.getGame().getLocation() + " <br/> " + date, ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);

		addComponent(homeTeamComp);
		addComponent(awayTeamComp);
		addComponent(infoLabel);
	}

	private Component createTeamComponent(Contestant contestant, Integer score) {
		return Optional.ofNullable(contestant)
				.map(GridUtil::createTeamLayout)
				.map(teamLayout -> {
					teamLayout.addComponent(new Label(score != null ? score.toString() : "-"));
					return teamLayout;
				})
				.orElse(new HorizontalLayout(new Label("TBD")));
	}
}
