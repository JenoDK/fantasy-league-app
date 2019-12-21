package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.util.Objects;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class UserPredictionScoresGrid extends CustomGrid<UserPredictionScoreBean> {

	private final BehaviorSubject<UserPredictionScoreBean> viewAllScoresClicked = BehaviorSubject.create();

	public UserPredictionScoresGrid() {
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
		addColumn(bean -> OverviewUtil.getScoreWithWinner(bean.getGameHome_team_score(), bean.getGameAway_team_score(), bean.getGameHomeTeamWon()))
				.setHeader("Score")
				.setClassNameGenerator(item -> "v-align-center");
		addColumn(new ComponentRenderer<>(userPredictionScoreBean -> OverviewUtil.getTeamComponent(
						userPredictionScoreBean.getAway_team(),
						userPredictionScoreBean.getGame().getAway_team_placeholder(),
						userPredictionScoreBean.getAwayTeamWeight())))
				.setHeader("Team B");
		Column<UserPredictionScoreBean> dateColumn =
				addColumn(item -> DateUtil.DATE_TIME_FORMATTER.format(item.getGame().getGameDateTime()))
						.setHeader("Date");
		addColumn(bean -> OverviewUtil.getPredictionColumn(
						bean.getPredictionHome_team_score(),
						bean.getPredictionAway_team_score(),
						bean.getPredictionHomeTeamWon(),
						bean.predictionIsHidden(),
						bean.getPredictionHiddenUntil()))
				.setHeader("Prediction")
				.setClassNameGenerator(item -> {
					String baseStyle = "v-align-center";
					if (item.predictionIsHidden() ||
							(Objects.isNull(item.getPredictionHome_team_score()) || Objects.isNull(item.getPredictionAway_team_score()))) {
						baseStyle = baseStyle + " grid-cell-tiny-text";
					}
					return baseStyle;
				});
		Column<UserPredictionScoreBean> userScoreColumn =
				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getPredictionScore()))
						.setHeader(Resources.getMessage("pointsEarned"));
		userScoreColumn.setId("predictionScore");
		addColumn(new ComponentRenderer<>(this::createViewAllResultsButton))
				.setHeader("")
				.setWidth("70px")
				.setId("seeAllResultsButton");
		sort(Lists.newArrayList(new GridSortOrder<>(dateColumn, SortDirection.ASCENDING)));
	}

	private Button createViewAllResultsButton(UserPredictionScoreBean bean) {
		Button viewAllScoresButton = new Button(VaadinIcon.BULLETS.create());
//		viewAllScoresButton.addClassName(ValoTheme.BUTTON_TINY);
		viewAllScoresButton.addClickListener(ignored -> viewAllScoresClicked.onNext(bean));
		return viewAllScoresButton;
	}

	public Observable<UserPredictionScoreBean> viewAllResultsClicked() {
		return viewAllScoresClicked;
	}
}
