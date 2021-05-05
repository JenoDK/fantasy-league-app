package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.usertotalscore;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBindingModel;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.UserScoreBean;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

import io.reactivex.subjects.BehaviorSubject;

@Tag("user-total-score-card")
@JsModule("./src/views/overview/user-total-score-card.js")
public class UserTotalScoreCard extends PolymerTemplate<MatchBindingModel> {

	private final UserScoreBean bean;
	private final BehaviorSubject<UserScoreBean> kudo;

	@Id("user")
	private H4 user;

	@Id("groupScore")
	private Span groupScore;
	@Id("eighthFinals")
	private Span eighthFinals;
	@Id("quarterFinals")
	private Span quarterFinals;
	@Id("semiFinals")
	private Span semiFinals;
	@Id("finals")
	private Span finals;
	@Id("total")
	private H4 total;

	public UserTotalScoreCard(UserScoreBean bean, BehaviorSubject<UserScoreBean> kudo) {
		this.bean = bean;
		this.kudo = kudo;

		initLayout();
	}

	private void initLayout() {
		Image profileIcon = LayoutUtil.initUserH4(user, bean.getUser(), bean.getPosition() + ". " + bean.getUser().getUsername());
		profileIcon.setId("user_profile_icon_" + bean.getPosition());
		groupScore.setText(getStageScore(SoccerCupStages.GROUP_PHASE));
		eighthFinals.setText(getStageScore(SoccerCupStages.EIGHTH_FINALS));
		quarterFinals.setText(getStageScore(SoccerCupStages.QUARTER_FINALS));
		semiFinals.setText(getStageScore(SoccerCupStages.SEMI_FINALS));
		finals.setText(getStageScore(SoccerCupStages.FINALS));
		total.setText(Resources.getMessage("totalScore", OverviewUtil.getScoreFormatted(bean.getTotalScore())));
	}

	private String getStageScore(SoccerCupStages groupPhase) {
		return Resources.getMessage("score." + groupPhase.getName(), OverviewUtil.getScoreFormatted(bean.getScore(groupPhase)));
	}

}
