package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction;

import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.upcomingmatches.GameBean;
import com.jeno.fantasyleague.util.GridUtil;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ComponentRenderer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.time.LocalDateTime;

public class PredictionGrid extends CustomGrid<PredictionBean> {

	private final BehaviorSubject<Object> scoreChanged = BehaviorSubject.create();

	public PredictionGrid() {
		super();
		initGrid();
	}

	private void initGrid() {
		addColumn(predictionBean -> GridUtil.createTeamLayout(predictionBean.getHomeTeam()), new ComponentRenderer())
			.setCaption("Team A");
		addColumn(predictionBean -> getScoreLayout(predictionBean), new ComponentRenderer())
			.setCaption("Score")
			.setStyleGenerator(item -> "v-align-center");
		addColumn(predictionBean -> GridUtil.createTeamLayout(predictionBean.getAwayTeam()), new ComponentRenderer())
			.setCaption("Team B");
	}

	private HorizontalLayout getScoreLayout(PredictionBean predictionBean) {
		if (LocalDateTime.now().isBefore(predictionBean.getGame().getGame_date_time())) {
			return GridUtil.getTextFieldScoreLayout(
					predictionBean,
					PredictionBean::getHomeTeamScore,
					PredictionBean::setHomeTeamScore,
					PredictionBean::getAwayTeamScore,
					PredictionBean::setAwayTeamScore,
					scoreChanged);
		} else {
			HorizontalLayout layout = new HorizontalLayout();
			layout.addComponent(new Label(GameBean.getTeamScore(predictionBean.getHomeTeamScore()) + " - " + GameBean.getTeamScore(predictionBean.getAwayTeamScore())));
			return layout;
		}
	}

	public Observable<Object> scoreChanged() {
		return scoreChanged;
	}
}
