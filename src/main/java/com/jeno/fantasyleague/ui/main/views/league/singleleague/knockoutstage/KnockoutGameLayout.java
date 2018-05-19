package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Optional;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class KnockoutGameLayout extends VerticalLayout {

	protected final SingleLeagueServiceProvider singleLeagueServiceprovider;
	protected final League league;

	public KnockoutGameLayout(SingleLeagueServiceProvider singleLeagueServiceprovider, League league, KnockoutGameBean game) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;

		setWidth(230f, Unit.PIXELS);
		setMargin(true);

		addStyleName(ValoTheme.LAYOUT_CARD);
		addStyleName("bracket-game");

		HorizontalLayout homeTeamComp = createHomeTeamComponent(game);
		HorizontalLayout awayTeamComp = createAwayTeamComponent(game);

		String date = DateUtil.DATE_TIME_FORMATTER.format(game.getGame().getGame_date_time());
		Label infoLabel = new Label(game.getGame().getLocation() + " <br/> " + date, ContentMode.HTML);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);

		addComponent(homeTeamComp);
		addComponent(awayTeamComp);
		addComponent(infoLabel);
	}

	protected abstract HorizontalLayout createHomeTeamComponent(KnockoutGameBean game);

	protected abstract HorizontalLayout createAwayTeamComponent(KnockoutGameBean game);

	protected HorizontalLayout createTeamComponent(Contestant contestant, String teamPlaceHolder) {
		return Optional.ofNullable(contestant)
				.map(GridUtil::createTeamLayout)
				.orElse(new HorizontalLayout(new Label(teamPlaceHolder)));
	}
}
