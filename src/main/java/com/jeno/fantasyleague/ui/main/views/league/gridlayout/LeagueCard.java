package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.LeagueImageResourceCache;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.reactivex.subjects.BehaviorSubject;

import java.util.stream.Collectors;

@Tag("league-card")
@JsModule("./src/views/league/league-card.js")
public class LeagueCard extends PolymerTemplate<TemplateModel> {

	private final LeagueBean league;
	private final BehaviorSubject<LeagueBean> clickedLeague;

	@Id("content")
	private Div content;

	@Id("wrapper")
	private VerticalLayout wrapper;

	@Id("leagueImageTag")
	private Image img;

	@Id("leagueName")
	private H2 name;

	@Id("adminName")
	private Span adminName;

	@Id("membersCount")
	private Span membersCount;

	public LeagueCard(LeagueBean league, BehaviorSubject<LeagueBean> clickedLeague) {
		this.league = league;
		this.clickedLeague = clickedLeague;

		initLayout();
	}

	private void initLayout() {
		League league = this.league.getLeague();
		img.setAlt("league_banner");
		if (league.getLeague_picture() != null) {
			img.setSrc(LeagueImageResourceCache.addOrGetLeagueImageResource(league));
		} else {
			img.addClassName("default-league-banner");
		}
		wrapper.addClickListener(ignored -> clickedLeague.onNext(this.league));
		name.setText(league.getName());
		adminName.setText("Owners: " + this.league.getLeagueOwners().stream().map(User::getUsername).collect(Collectors.joining(", ")));
		membersCount.setText("Members: " + this.league.getLeagueUsers().size());
		wrapper.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, img);
	}

}
