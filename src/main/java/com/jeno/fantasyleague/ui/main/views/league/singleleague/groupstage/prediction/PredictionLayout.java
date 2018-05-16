package com.jeno.fantasyleague.ui.main.views.league.singleleague.groupstage.prediction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jeno.fantasyleague.model.ContestantGroup;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PredictionLayout extends VerticalLayout {

	public PredictionLayout(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		super();
		setSpacing(false);
		setMargin(false);

		Label groupLabel = new Label(group.getName(), ContentMode.HTML);
		groupLabel.addStyleName(ValoTheme.LABEL_H3);

		PredictionGrid predictionsGrid = new PredictionGrid();
		predictionsGrid.setItems(getPredictions(singleLeagueService, league, group));

		Button savePredictionsButton = new Button("Update prediction", VaadinIcons.USER_CHECK);
		savePredictionsButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		savePredictionsButton.addStyleName(ValoTheme.BUTTON_TINY);
		savePredictionsButton.setVisible(false);
		savePredictionsButton.addClickListener(ignored -> {
			List<Prediction> predictions = predictionsGrid.getItems().stream()
					.filter(PredictionBean::scoreChangedAndIsValid)
					.filter(predictionBean -> LocalDateTime.now().isBefore(predictionBean.getGame().getGame_date_time()))
					.map(PredictionBean::setPredictionsAndGetModelItem)
					.collect(Collectors.toList());
			singleLeagueService.getPredictionRepository().saveAll(predictions);
			savePredictionsButton.setVisible(false);
		});
		predictionsGrid.scoreChanged()
				.map(isValid -> predictionsGrid.getItems().stream().anyMatch(PredictionBean::scoreChangedAndIsValid) && isValid)
				.subscribe(savePredictionsButton::setVisible);

		HorizontalLayout titleLayout = new HorizontalLayout();
		titleLayout.setWidth(430, Unit.PIXELS);
		titleLayout.setSpacing(true);
		titleLayout.addComponent(groupLabel);
		titleLayout.addComponent(savePredictionsButton);
		titleLayout.setComponentAlignment(savePredictionsButton, Alignment.MIDDLE_RIGHT);
		titleLayout.setExpandRatio(groupLabel, 8);
		titleLayout.setExpandRatio(savePredictionsButton, 2);
		addComponent(titleLayout);
		addComponent(predictionsGrid);
	}

	private List<PredictionBean> getPredictions(SingleLeagueServiceProvider singleLeagueService, League league, ContestantGroup group) {
		Map<Long, Game> games = singleLeagueService.getGameRepository().findByLeagueAndGroupStageAndJoinTeams(league, group).stream()
				.collect(Collectors.toMap(Game::getId, Function.identity()));
		return singleLeagueService.getLoggedInUserPredictions(Lists.newArrayList(games.values())).stream()
				// Like this because we want the Contestants in the game entity.
				.map(prediction -> new PredictionBean(games.get(prediction.getGame().getId()), prediction))
				.collect(Collectors.toList());
	}
}
