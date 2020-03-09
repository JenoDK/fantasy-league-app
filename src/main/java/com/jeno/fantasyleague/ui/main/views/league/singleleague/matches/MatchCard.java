package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.ui.common.field.BasicRadioButtonGroup;
import com.jeno.fantasyleague.ui.common.label.PredictionStatusLabel;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.renderer.TextRenderer;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Tag("match-card")
@JsModule("./src/views/match/match-card.js")
public class MatchCard extends PolymerTemplate<MatchBindingModel> {

	private final MatchBean match;
	private final BehaviorSubject<MatchBean> clickedMatch;
	private final BehaviorSubject<MatchPredictionBean> predictionChanged = BehaviorSubject.create();
	private final BehaviorSubject<MatchPredictionBean> scoreChange = BehaviorSubject.create();

	private MatchPredictionBean predictionBean;

	@Id("content")
	private Div content;

	@Id("wrapper")
	private VerticalLayout wrapper;

	@Id("match-wrapper")
	private Div matchWrapper;

	@Id("prediction-button")
	private Button predictionButton;

	@Id("your-prediction-wrapper")
	private Div yourPredictionWrapper;

	private PredictionStatusLabel yourPredictionLabel;

	/**
	 * @param match the match bean
	 * @param clickedMatch click handler, null if this shouldn't be interacted with
	 */
	public MatchCard(MatchBean match, BehaviorSubject<MatchBean> clickedMatch) {
		this.match = match;
		this.predictionBean = new MatchPredictionBean(match.getPrediction());
		this.clickedMatch = clickedMatch;

		initLayout();
	}

	public Observable<MatchPredictionBean> predictionChanged() {
		return predictionChanged;
	}

	@ClientCallable
	private void handleClick() {
		if (clickedMatch != null) {
			clickedMatch.onNext(match);
		}
	}

	@ClientCallable
	private void openPrediction() {
		new PopupWindow.Builder("Prediction", this::createPredictionLayout)
				.setType(PopupWindow.Type.CONFIRM)
				.sizeUndefined(true)
				.build()
				.open();
	}

	private void initLayout() {
		getModel().setMatch(new MatchCardBean(match));
		if (clickedMatch != null) {
			wrapper.getThemeList().add("intractable");
		}
		wrapper.setPadding(false);
		initMatchLayout();
		yourPredictionLabel = new PredictionStatusLabel("yourPrediction");
		match.predictionCHanged().subscribe(b -> {
			predictionBean = new MatchPredictionBean(match.getPrediction());
			setPredictionStatusText();
		});
		setPredictionStatusText();
		yourPredictionWrapper.add(yourPredictionLabel);
		boolean matchIsEditable = match.getAwayTeam() != null && match.getHomeTeam() != null && nowIsBeforeMatch();
		predictionButton.setEnabled(matchIsEditable);
	}

	private void setPredictionStatusText() {
		yourPredictionLabel.setPredictionStatusText(
				predictionBean.getHomeTeamPrediction(),
				predictionBean.getAwayTeamPrediction(),
				predictionBean.getHomeTeamPredictionIsWinner(),
				match.predictionIsHidden(),
				match.getPredictionHiddenUntil());
	}

	private VerticalLayout createPredictionLayout(PopupWindow dialog) {
		VerticalLayout layout = new VerticalLayout();
		layout.setAlignItems(FlexComponent.Alignment.CENTER);

		VerticalLayout predictionLayout = new VerticalLayout();
		predictionLayout.setPadding(false);
		predictionLayout.setAlignItems(FlexComponent.Alignment.END);
		layout.add(predictionLayout);

		HorizontalLayout left = new HorizontalLayout();
		left.setAlignItems(FlexComponent.Alignment.CENTER);
		left.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		left.add(LayoutUtil.createTeamLayout(true, match.getHomeTeam(), match.getGame().getHome_team_placeholder()));

		HorizontalLayout right = new HorizontalLayout();
		right.setAlignItems(FlexComponent.Alignment.CENTER);
		right.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		right.add(LayoutUtil.createTeamLayout(true, match.getAwayTeam(), match.getGame().getAway_team_placeholder()));

		if (nowIsBeforeMatch()) {
			StatusLabel statusLabel = new StatusLabel();
			statusLabel.setVisible(false);

			Binder<MatchPredictionBean> binder = new Binder<>();
			binder.setValidationStatusHandler(s -> {
				if (s.hasErrors()) {
					var msg = s.getValidationErrors().stream()
							.map(ValidationResult::getErrorMessage)
							.distinct()
							.collect(Collectors.joining("\n"));
					statusLabel.setErrorText(msg);
				}
				statusLabel.setVisible(s.hasErrors());
			});

			binder.setBean(predictionBean);

			left.add(LayoutUtil.createPositiveIntegerTextField(binder, MatchPredictionBean::getHomeTeamPrediction, MatchPredictionBean::setHomeTeamPrediction));
			right.add(LayoutUtil.createPositiveIntegerTextField(binder, MatchPredictionBean::getAwayTeamPrediction, MatchPredictionBean::setAwayTeamPrediction));

			if (!SoccerCupStages.GROUP_PHASE.toString().equals(match.getGame().getStage())) {
				VerticalLayout winnerWrapper = new VerticalLayout();
				winnerWrapper.setMargin(false);
				winnerWrapper.setPadding(false);
				winnerWrapper.setSpacing(false);
				winnerWrapper.setVisible(scoreNotNullAndEqual(predictionBean));
				BasicRadioButtonGroup winnerSelection = new BasicRadioButtonGroup();
				winnerSelection.setLabel("Winner");
				winnerSelection.addClassName("winner-selection");
				BasicRadioButtonGroup.RadioButtonItem homeTeam = new BasicRadioButtonGroup.RadioButtonItem("homeTeam", Optional.ofNullable(match.getHomeTeam()).map(Contestant::getName).orElse(match.getGame().getHome_team_placeholder()));
				BasicRadioButtonGroup.RadioButtonItem awayTeam = new BasicRadioButtonGroup.RadioButtonItem("awayTeam", Optional.ofNullable(match.getAwayTeam()).map(Contestant::getName).orElse(match.getGame().getAway_team_placeholder()));
				winnerSelection.setItems(List.of(homeTeam, awayTeam));
				winnerSelection.setRenderer(new TextRenderer<>(BasicRadioButtonGroup.RadioButtonItem::getValue));
				if (predictionBean.getHomeTeamPredictionIsWinner().isPresent()) {
					winnerSelection.setValue(predictionBean.getHomeTeamPredictionIsWinner().get() ? homeTeam : awayTeam);
				}
				winnerSelection.addValueChangeListener(event -> predictionBean.setHomeTeamPredictionIsWinner(homeTeam.equals(event.getValue())));
				winnerWrapper.add(winnerSelection);
				binder.addValueChangeListener(ignored -> winnerWrapper.setVisible(scoreNotNullAndEqual(predictionBean)));
				predictionLayout.add(winnerWrapper);
			}

			dialog.setOnConfirm(() -> {
				boolean valid = binder.isValid();
				if (valid) {
					predictionChanged.onNext(predictionBean);
					setPredictionStatusText();
					dialog.close();
				}
				return valid;
			});
			layout.add(statusLabel);
		} else {
			left.add(new Label("" + match.getHomeTeamPrediction()));
			right.add(new Label("" + match.getAwayTeamPrediction()));
		}

		predictionLayout.add(left);
		predictionLayout.add(right);

		return layout;
	}

	private boolean nowIsBeforeMatch() {
		return LocalDateTime.now().isBefore(match.getGame().getGameDateTime());
	}

	public boolean scoreNotNullAndEqual(MatchPredictionBean bean) {
		return
				Objects.nonNull(bean.getHomeTeamPrediction()) &&
				Objects.nonNull(bean.getAwayTeamPrediction()) &&
				bean.getHomeTeamPrediction().equals(bean.getAwayTeamPrediction());
	}

	private void initMatchLayout() {
		HorizontalLayout left = LayoutUtil.createTeamLayout(true, match.getHomeTeam(), match.getGame().getHome_team_placeholder());
		left.addClassName("left");
		left.setWidthFull();
		left.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		left.getStyle().set("margin-right", "20px");
		HorizontalLayout right = LayoutUtil.createTeamLayout(false, match.getAwayTeam(), match.getGame().getAway_team_placeholder());
		right.addClassName("right");
		right.setWidthFull();
		right.getStyle().set("margin-left", "20px");
		matchWrapper.add(left);
		Label scoreLabel = new Label(
				OverviewUtil.getScoreWithWinner(match.getGameHomeTeamScore(), match.getGameAwayTeamScore(), match.getGameHomeTeamWon()));
		scoreLabel.addClassName("score");
		scoreLabel.getStyle().set("text-align", "center").set("flex", "none");
		matchWrapper.add(scoreLabel);
		matchWrapper.add(right);
	}
}
