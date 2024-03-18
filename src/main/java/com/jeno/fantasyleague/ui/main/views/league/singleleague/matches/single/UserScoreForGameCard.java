package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.single;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.label.PredictionStatusLabel;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBindingModel;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Objects;

@Tag("user-score-for-game-card")
@JsModule("./src/views/match/user-score-for-game-card.js")
public class UserScoreForGameCard extends PolymerTemplate<MatchBindingModel> {

	private final UserScoresForGameBean bean;
	private final BehaviorSubject<UserScoresForGameBean> kudo;
	private final Contestant away_team;
	private final Contestant home_team;

	@Id("user")
	private H4 user;

	@Id("prediction")
	private Div prediction;

	@Id("points")
	private H4 points;

	@Id("stocksTeam1")
	private Span stocksTeam1;

	@Id("stocksTeam2")
	private Span stocksTeam2;

	public UserScoreForGameCard(UserScoresForGameBean bean, Contestant home_team, Contestant away_team, BehaviorSubject<UserScoresForGameBean> kudo) {
		this.bean = bean;
		this.home_team = home_team;
		this.away_team = away_team;
		this.kudo = kudo;

		initLayout();
	}

	private void initLayout() {
		LayoutUtil.initUserH4(user, bean.getUser());

		PredictionStatusLabel yourPredictionLabel = new PredictionStatusLabel("prediction");
		yourPredictionLabel.setPredictionStatusText(
				bean.getHomePrediction(),
				bean.getAwayPrediction(),
				bean.getHomeTeamWon(),
				bean.isPredictionIsHiddenForUser(),
				bean.getPredictionHiddenUntil());
		prediction.add(yourPredictionLabel);

		if (bean.isPredictionIsHiddenForUser() || (Objects.isNull(bean.getHomePrediction()) || Objects.isNull(bean.getAwayPrediction()))) {
			points.getStyle().set("display", "none");
		} else {
			points.setText(Resources.getMessage("points") + ": " + OverviewUtil.getScoreFormatted(bean.getScore()).toString());
		}

		initStocksLabel(stocksTeam1, home_team, bean.getHomeTeamWeight());
		initStocksLabel(stocksTeam2, away_team, bean.getAwayTeamWeight());
	}
	private void initStocksLabel(Span label, Contestant contestant, Integer stocks) {
		if (contestant != null) {
			label.setText(stocks + " " + Resources.getMessage("stocks"));
			Image icon = new Image();
			icon.setWidth("42px");
			icon.setHeight("28px");
			icon.setSrc(contestant.getIcon_path());
			label.addComponentAsFirst(icon);
		} else {
			label.addComponentAsFirst(VaadinIcon.QUESTION.create());
		}
	}

}
