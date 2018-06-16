package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview;

import java.math.BigDecimal;
import java.util.Objects;

import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class UserPredictionScoresGrid extends CustomGrid<UserPredictionScoreBean> {

	private final BehaviorSubject<UserPredictionScoreBean> viewAllScoresClicked = BehaviorSubject.create();

	public UserPredictionScoresGrid() {
		super();
		setWidth(100, Unit.PERCENTAGE);
		initColumns();
	}

	private void initColumns() {
		addColumn(
				userPredictionScoreBean -> OverviewUtil.getTeamComponent(
						userPredictionScoreBean.getHome_team(),
						userPredictionScoreBean.getGame().getHome_team_placeholder(),
						userPredictionScoreBean.getHomeTeamWeight()),
				new ComponentRenderer())
				.setCaption("Team A");
		addColumn(bean -> OverviewUtil.getScoreWithWinner(bean.getGameHome_team_score(), bean.getGameAway_team_score(), bean.getGameHomeTeamWon()))
				.setCaption("Score")
				.setStyleGenerator(item -> "v-align-center");
		addColumn(userPredictionScoreBean -> OverviewUtil.getTeamComponent(
						userPredictionScoreBean.getAway_team(),
						userPredictionScoreBean.getGame().getAway_team_placeholder(),
						userPredictionScoreBean.getAwayTeamWeight()),
				new ComponentRenderer())
				.setCaption("Team B");
		Column<UserPredictionScoreBean, String> dateColumn =
				addColumn(item -> DateUtil.DATE_TIME_FORMATTER.format(item.getGame().getGameDateTime()))
						.setCaption("Date");
		addColumn(bean -> OverviewUtil.getPredictionColumn(
						bean.getPredictionHome_team_score(),
						bean.getPredictionAway_team_score(),
						bean.getPredictionHomeTeamWon(),
						bean.predictionIsHidden(),
						bean.getPredictionHiddenUntil()))
				.setCaption("Prediction")
				.setStyleGenerator(item -> {
					String baseStyle = "v-align-center";
					if (item.predictionIsHidden() ||
							(Objects.isNull(item.getPredictionHome_team_score()) || Objects.isNull(item.getPredictionAway_team_score()))) {
						baseStyle = baseStyle + " grid-cell-tiny-text";
					}
					return baseStyle;
				});
		Column<UserPredictionScoreBean, BigDecimal> userScoreColumn =
				addColumn(bean -> OverviewUtil.getScoreFormatted(bean.getPredictionScore()))
						.setCaption(Resources.getMessage("pointsEarned"))
						.setId("predictionScore");
		addColumn(this::createViewAllResultsButton, new ComponentRenderer())
				.setCaption("")
				.setId("seeAllResultsButton")
				.setWidth(70);
		setSortOrder(new GridSortOrderBuilder().thenAsc(dateColumn));
	}

	private Button createViewAllResultsButton(UserPredictionScoreBean bean) {
		Button viewAllScoresButton = new Button(VaadinIcons.BULLETS);
		viewAllScoresButton.addStyleName(ValoTheme.BUTTON_TINY);
		viewAllScoresButton.addClickListener(ignored -> viewAllScoresClicked.onNext(bean));
		return viewAllScoresButton;
	}

	public Observable<UserPredictionScoreBean> viewAllResultsClicked() {
		return viewAllScoresClicked;
	}
}
