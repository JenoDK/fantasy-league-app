package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Objects;

import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class UpcomingMatchesGrid extends CustomGrid<UserPredictionScoreBean> {

	private final BehaviorSubject<UserPredictionScoreBean> viewAllScoresClicked = BehaviorSubject.create();

	public UpcomingMatchesGrid() {
		super();
		setWidth("100%");
		initColumns();
	}

	private void initColumns() {
		addColumn(
				new ComponentRenderer<>(userPredictionScoreBean -> OverviewUtil.getTeamComponent(
						userPredictionScoreBean.getHome_team(),
						userPredictionScoreBean.getGame().getHome_team_placeholder(),
						userPredictionScoreBean.getHomeTeamWeight())))
				.setHeader("Team A");
		addColumn(bean -> OverviewUtil.getPredictionColumn(
						bean.getPredictionHome_team_score(),
						bean.getPredictionAway_team_score(),
						bean.getPredictionHomeTeamWon(),
						bean.predictionIsHidden(),
						bean.getPredictionHiddenUntil()))
				.setHeader("Prediction")
				.setTextAlign(ColumnTextAlign.CENTER)
				.setClassNameGenerator(item -> {
					if (item.predictionIsHidden() ||
							(Objects.isNull(item.getPredictionHome_team_score()) || Objects.isNull(item.getPredictionAway_team_score()))) {
						return "grid-cell-tiny-text";
					}
					return "";
				});
		addColumn(new ComponentRenderer<>(userPredictionScoreBean -> OverviewUtil.getTeamComponent(
						userPredictionScoreBean.getAway_team(),
						userPredictionScoreBean.getGame().getAway_team_placeholder(),
						userPredictionScoreBean.getAwayTeamWeight())))
				.setHeader("Team B");
		addColumn(new ComponentRenderer<>(this::createViewAllResultsButton))
				.setHeader("")
				.setAutoWidth(true)
				.setId("seeAllResultsButton");
	}

	private Button createViewAllResultsButton(UserPredictionScoreBean bean) {
		Button viewAllScoresButton = new Button(VaadinIcon.BULLETS.create());
		viewAllScoresButton.addClickListener(ignored -> viewAllScoresClicked.onNext(bean));
		return viewAllScoresButton;
	}

	public Observable<UserPredictionScoreBean> viewAllResultsClicked() {
		return viewAllScoresClicked;
	}
}
