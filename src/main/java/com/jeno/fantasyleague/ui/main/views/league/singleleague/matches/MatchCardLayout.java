package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.eufaeuro2020.UefaEuro2020Initializer;
import com.jeno.fantasyleague.backend.data.service.repo.game.GameServiceImpl;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.BasicRadioButtonGroup;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.label.PredictionStatusLabel;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.OverviewUtil;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@CssImport("./styles/shared-styles.css")
@CssImport("./styles/match-card-styles.css")
public class MatchCardLayout extends Div {

	private final BehaviorSubject<MatchBean> clickedMatch;
	private final BehaviorSubject<MatchPredictionBean> predictionChanged = BehaviorSubject.create();
	private final BehaviorSubject<MatchPredictionBean> scoreChanged = BehaviorSubject.create();
	private final boolean loggedInUserIsAdmin;
	private final boolean canAdjustContestants;
	private final SingleLeagueServiceProvider singleLeagueServiceProvider;

	private MatchBean match;
	private MatchPredictionBean predictionBean;

	private Div scoreWrapper;
	private Div matchWrapper;
	private Label scoreLabel;
	private CustomButton predictionButton;
	private PredictionStatusLabel yourPredictionLabel;
	private Div yourPredictionWrapper;

	public MatchCardLayout(
			MatchBean match,
			BehaviorSubject<MatchBean> clickedMatch,
			boolean loggedInUserIsAdmin,
			boolean canAdjustContestants,
			SingleLeagueServiceProvider singleLeagueServiceProvider) {
		this.match = match;
		this.predictionBean = new MatchPredictionBean(match.getLeague(), match.getPrediction());
		this.clickedMatch = clickedMatch;
		this.loggedInUserIsAdmin = loggedInUserIsAdmin;
		this.canAdjustContestants = canAdjustContestants;
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		initLayout();
	}

	public Observable<MatchPredictionBean> predictionChanged() {
		return predictionChanged;
	}

	public Observable<MatchPredictionBean> scoreChanged() {
		return scoreChanged;
	}

	private void initLayout() {
		setId("content");
		setClassName("content");

		Div infoWrapper = createInfoWrapper();
		scoreWrapper = createScoreWrapper();
		matchWrapper = createMatchWrapper();
		Div predictionWrapper = createPredictionWrapper();
		VerticalLayout wrapper = new VerticalLayout(infoWrapper, scoreWrapper, matchWrapper, predictionWrapper);
		wrapper.setId("wrapper");
		wrapper.setClassName("wrapper");
		if (clickedMatch != null) {
			wrapper.getThemeList().add("intractable");
			wrapper.addClickListener(event -> clickedMatch.onNext(match));
		}
		wrapper.setPadding(false);
		add(wrapper);
		yourPredictionLabel = new PredictionStatusLabel("yourPrediction");
		match.predictionCHanged().subscribe(b -> {
			predictionBean = new MatchPredictionBean(match.getLeague(), match.getPrediction());
			setPredictionStatusText();
		});
		setPredictionStatusText();
		yourPredictionWrapper.add(yourPredictionLabel);
		boolean matchIsEditable = match.getAwayTeam() != null && match.getHomeTeam() != null && nowIsBeforeMatch();
		predictionButton.setEnabled(matchIsEditable);
	}

	private Div createPredictionWrapper() {
		predictionButton = new CustomButton("Fill in prediction");
		predictionButton.setId("prediction-button");
		predictionButton.addPreventClickPropagation();
		predictionButton.addClickListener(ignored -> openPrediction());
		yourPredictionWrapper = new Div();
		yourPredictionWrapper.setId("your-prediction-wrapper");
		yourPredictionWrapper.setClassName("middle");
		H4 pointsGained = new H4(Resources.getMessage("points") + ": " + OverviewUtil.getScoreFormatted(match.getPredictionScore()));
		pointsGained.setClassName("right");
		Div predictionWrapper = new Div(predictionButton, yourPredictionWrapper, pointsGained);
		predictionWrapper.setClassName("prediction-wrapper");
		return predictionWrapper;
	}

	private Div createMatchWrapper() {
		Div matchWrapper = new Div();
		matchWrapper.setId("match-wrapper");
		Game game = match.getGame();
		boolean isEightFinalAndCanChooseContestant = SoccerCupStages.EIGHTH_FINALS.toString().equals(game.getStage()) &&
				LocalDateTime.now().isBefore(game.getGameDateTime()) &&
				loggedInUserIsAdmin &&
				canAdjustContestants;
		HorizontalLayout left;
		if (isEightFinalAndCanChooseContestant) {
			left = new HorizontalLayout();
			left.setPadding(true);
			left.add(getContestantComboBox(game, match.getHomeContestantChanged(), game.getHome_team_placeholder(), game.getHome_team(), game::setHome_team));
		} else {
			left = LayoutUtil.createTeamLayout(true, match.getHomeTeam(), game.getHome_team_placeholder());
			left.addClassName("left");
			left.setWidthFull();
			left.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
			left.getStyle().set("margin-right", "20px");
			match.getHomeContestantChanged().subscribe(homeTeam -> {
				if (homeTeam != null) {
					left.removeAll();
					left.add(LayoutUtil.createTeamLayout(true, homeTeam));
				}
			});
		}
		HorizontalLayout right;
		if (isEightFinalAndCanChooseContestant) {
			right = new HorizontalLayout();
			right.setPadding(true);
			right.addClassName("combobox-contestant-selector");
			right.add(getContestantComboBox(game, match.getAwayContestantChanged(), game.getAway_team_placeholder(), game.getAway_team(), game::setAway_team));
		} else {
			right = LayoutUtil.createTeamLayout(false, match.getAwayTeam(), game.getAway_team_placeholder());
			right.addClassName("right");
			right.setWidthFull();
			right.getStyle().set("margin-left", "20px");
			match.getAwayContestantChanged().subscribe(awayTeam -> {
				if (awayTeam != null) {
					right.removeAll();
					right.add(LayoutUtil.createTeamLayout(false, awayTeam));
				}
			});
		}
		matchWrapper.add(left);
		scoreLabel = new Label(getScoreWithWinnerText());
		scoreLabel.addClassName("score");
		scoreLabel.getStyle().set("text-align", "center").set("flex", "none");
		matchWrapper.add(scoreLabel);
		matchWrapper.add(right);
		if (loggedInUserIsAdmin) {
			scoreWrapper.setVisible(true);
		} else {
			scoreWrapper.setVisible(false);
		}
		return matchWrapper;
	}

	private Div createScoreWrapper() {
		CustomButton scoreButton = new CustomButton("Fill in score");
		scoreButton.addClickListener(ignored -> openScore());
		scoreButton.setId("score-button");
		scoreButton.addPreventClickPropagation();
		Div scoreWrapper = new Div(scoreButton);
		scoreWrapper.setId("score-wrapper");
		return scoreWrapper;
	}

	private void openScore() {
		Function<PopupWindow, Component> popupLayoutFunction = popupWindow -> {
			VerticalLayout layout = createPredictionLayout(
					popupWindow,
					MatchPredictionBean::getHomeTeamScore,
					MatchPredictionBean::setHomeTeamScore,
					MatchPredictionBean::getAwayTeamScore,
					MatchPredictionBean::setAwayTeamScore,
					scoreChanged,
					() -> predictionBean.getHomeTeamIsWinner(),
					homeTeamIsWinner -> predictionBean.setHomeTeamIsWinner(homeTeamIsWinner),
					true
			);
			CustomButton clearScores = new CustomButton("Clear scores");
			clearScores.addThemeName("small");
			clearScores.addClickListener(ignored -> {
				predictionBean.setAwayTeamScore(null);
				predictionBean.setHomeTeamScore(null);
				predictionBean.setHomeTeamIsWinnerOptional(Optional.empty());
			});
			layout.add(clearScores);
			return layout;
		};
		new PopupWindow.Builder("Score", popupLayoutFunction)
				.setType(PopupWindow.Type.CONFIRM)
				.sizeUndefined(true)
				.build()
				.open();
	}

	private void openPrediction() {
		Function<PopupWindow, Component> popupLayoutFunction = popupWindow -> createPredictionLayout(
				popupWindow,
				MatchPredictionBean::getHomeTeamPrediction,
				MatchPredictionBean::setHomeTeamPrediction,
				MatchPredictionBean::getAwayTeamPrediction,
				MatchPredictionBean::setAwayTeamPrediction,
				predictionChanged,
				() -> predictionBean.getHomeTeamPredictionIsWinner(),
				homeTeamIsWinner -> predictionBean.setHomeTeamPredictionIsWinner(homeTeamIsWinner),
				false
		);
		new PopupWindow.Builder("Prediction", popupLayoutFunction)
				.setType(PopupWindow.Type.CONFIRM)
				.sizeUndefined(true)
				.build()
				.open();
	}

	private Div createInfoWrapper() {
		H4 date = new H4(DateUtil.DATE_TIME_FORMATTER.format(match.getGame().getGameDateTime()));
		date.addClassName("left");
		H4 stage = new H4(SoccerCupStages.getLeagueStageTitle(match.getGame(), match.getHomeTeam() != null ? match.getHomeTeam() : match.getAwayTeam()));
		stage.addClassName("middle");
		H4 place = new H4(match.getGame().getLocation());
		place.addClassName("right");
		Div infoWrapper = new Div(date, stage, place);
		infoWrapper.setClassName("info-wrapper");
		return infoWrapper;
	}

	private void setPredictionStatusText() {
		yourPredictionLabel.setPredictionStatusText(
				predictionBean.getHomeTeamPrediction(),
				predictionBean.getAwayTeamPrediction(),
				predictionBean.getHomeTeamPredictionIsWinner(),
				match.predictionIsHidden(),
				match.getPredictionHiddenUntil());
	}

	private boolean nowIsBeforeMatch() {
		return LocalDateTime.now().isBefore(match.getGame().getGameDateTime());
	}

	private String getScoreWithWinnerText() {
		return OverviewUtil.getScoreWithWinner(match.getGameHomeTeamScore(), match.getGameAwayTeamScore(), match.getGameHomeTeamWon());
	}

	private ComboBox<Contestant> getContestantComboBox(Game game, BehaviorSubject<Contestant> contestantChanged, String placeHolder, Contestant possibleContestant, Consumer<Contestant> contestantConsumer) {
		ListDataProvider<Contestant> dataProvider = getDataProvider(GameServiceImpl.getGroups(placeHolder));
		ComboBox<Contestant> contestantCombobox = new ComboBox<>(placeHolder);
		contestantCombobox.addClassName("contestantSelection");
		contestantCombobox.setPlaceholder(placeHolder);
		contestantCombobox.setDataProvider(dataProvider);
		contestantCombobox.setItemLabelGenerator(Contestant::getName);
		contestantCombobox.setRenderer(new ComponentRenderer<>(contestant -> {
			HorizontalLayout div = new HorizontalLayout();
			div.setAlignItems(FlexComponent.Alignment.CENTER);
			div.add(new Label(contestant.getContestant_group().getName().replace("Group ", "")));
			div.add(new Image(contestant.getIcon_path(), contestant.getName()));
			div.add(new Label(contestant.getName()));
			return div;
		}));
		if (possibleContestant != null) {
			singleLeagueServiceProvider.getContestantRepository().findById(possibleContestant.getId()).ifPresent(contestantCombobox::setValue);
		}
		contestantCombobox.addValueChangeListener(event -> {
			if (loggedInUserIsAdmin) {
				contestantConsumer.accept(event.getValue());
				Game updatedGame = singleLeagueServiceProvider.getGameRepository().saveAndFlush(game);
				contestantChanged.onNext(event.getValue());
				dataProvider.refreshAll();
				if (updatedGame.getHome_team() != null && updatedGame.getAway_team() != null && scoreWrapper != null) {
					scoreWrapper.setEnabled(true);
					predictionButton.setEnabled(true);
				}
			} else {
				Notification.show(Resources.getMessage("adminRightsRevoked"));
			}
		});
		contestantCombobox.getElement().addEventListener("click", event -> {}).addEventData("event.stopPropagation()");
		return contestantCombobox;
	}

	private ListDataProvider<Contestant> getDataProvider(List<UefaEuro2020Initializer.Group> groups) {
		return new ListDataProvider<>(singleLeagueServiceProvider.getContestantService().getPossibleContestantsFromGroupStage(groups, match.getLeague()));
	}

	private VerticalLayout createPredictionLayout(
			PopupWindow dialog,
			ValueProvider<MatchPredictionBean, Integer> homeGetter,
			Setter<MatchPredictionBean, Integer> homeSetter,
			ValueProvider<MatchPredictionBean, Integer> awayGetter,
			Setter<MatchPredictionBean, Integer> awaySetter,
			BehaviorSubject<MatchPredictionBean> valueChanged,
			Supplier<Optional<Boolean>> homeTeamIsWinnerSupplier,
			Consumer<Boolean> homeTeamIsWinnerChanged,
			boolean isScore) {
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

		if (isScore || nowIsBeforeMatch()) {
			StatusLabel statusLabel = new StatusLabel();
			statusLabel.setVisible(false);

			Binder<MatchPredictionBean> binder = new Binder<>();
			binder.setValidationStatusHandler(LayoutUtil.getDefaultBinderValidationStatusHandler(statusLabel));
			binder.setBean(predictionBean);

			TextField homeField = LayoutUtil.createPositiveIntegerTextField(binder, homeGetter, homeSetter);
			left.add(homeField);
			TextField awayField = LayoutUtil.createPositiveIntegerTextField(binder, awayGetter, awaySetter);
			right.add(awayField);

			if (!SoccerCupStages.GROUP_PHASE.toString().equals(match.getGame().getStage())) {
				VerticalLayout winnerWrapper = new VerticalLayout();
				winnerWrapper.setMargin(false);
				winnerWrapper.setPadding(false);
				winnerWrapper.setSpacing(false);
				winnerWrapper.setVisible(scoreNotNullAndEqual(predictionBean, homeGetter, awayGetter));
				BasicRadioButtonGroup winnerSelection = new BasicRadioButtonGroup();
				winnerSelection.setLabel("Winner");
				winnerSelection.addClassName("winner-selection");
				BasicRadioButtonGroup.RadioButtonItem homeTeam = new BasicRadioButtonGroup.RadioButtonItem("homeTeam", Optional.ofNullable(match.getHomeTeam()).map(Contestant::getName).orElse(match.getGame().getHome_team_placeholder()));
				BasicRadioButtonGroup.RadioButtonItem awayTeam = new BasicRadioButtonGroup.RadioButtonItem("awayTeam", Optional.ofNullable(match.getAwayTeam()).map(Contestant::getName).orElse(match.getGame().getAway_team_placeholder()));
				winnerSelection.setItems(List.of(homeTeam, awayTeam));
				winnerSelection.setRenderer(new TextRenderer<>(BasicRadioButtonGroup.RadioButtonItem::getValue));
				if (homeTeamIsWinnerSupplier.get().isPresent()) {
					winnerSelection.setValue(homeTeamIsWinnerSupplier.get().get() ? homeTeam : awayTeam);
				}
				winnerSelection.addValueChangeListener(event -> homeTeamIsWinnerChanged.accept(homeTeam.equals(event.getValue())));
				winnerWrapper.add(winnerSelection);
				homeField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> winnerWrapper.setVisible(scoreNotNullAndEqual(predictionBean, homeGetter, awayGetter)));
				awayField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> winnerWrapper.setVisible(scoreNotNullAndEqual(predictionBean, homeGetter, awayGetter)));
				predictionLayout.add(winnerWrapper);
			}

			dialog.setOnConfirm(() -> {
				boolean valid = binder.isValid();
				if (valid) {
					valueChanged.onNext(predictionBean);
					if (isScore) {
						scoreLabel.setText(getScoreWithWinnerText());
					} else {
						setPredictionStatusText();
					}
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

	public boolean scoreNotNullAndEqual(MatchPredictionBean bean, ValueProvider<MatchPredictionBean, Integer> homeGetter, ValueProvider<MatchPredictionBean, Integer> awayGetter) {
		Integer home = homeGetter.apply(bean);
		Integer away = awayGetter.apply(bean);
		return
				Objects.nonNull(home) &&
						Objects.nonNull(away) &&
						home.equals(away);
	}

}
