package com.jeno.fantasyleague.ui.main.views.league.singleleague.matches;

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
import com.vaadin.flow.component.html.Span;
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

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@CssImport("./styles/shared-styles.css")
@CssImport("./styles/match-card-styles.css")
public class MatchCardLayout extends Div {

	private final BehaviorSubject<MatchBean> clickedMatch;
	private final BehaviorSubject<MatchPredictionBean> predictionChanged = BehaviorSubject.create();
	private final BehaviorSubject<MatchPredictionBean> scoreChanged = BehaviorSubject.create();
	private final boolean loggedInUserIsAdmin;
	private final boolean isForSuperAdmin;
	private final boolean canAdjustContestants;
	private final boolean showDetails;
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
			boolean isForSuperAdmin,
			boolean canAdjustContestants,
			boolean showDetails,
			SingleLeagueServiceProvider singleLeagueServiceProvider) {
		this.match = match;
		this.predictionBean = new MatchPredictionBean(match.getLeague(), match.getPrediction());
		this.clickedMatch = clickedMatch;
		this.loggedInUserIsAdmin = loggedInUserIsAdmin;
		this.isForSuperAdmin = isForSuperAdmin;
		this.canAdjustContestants = canAdjustContestants;
		this.showDetails = showDetails;
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

		VerticalLayout wrapper = new VerticalLayout(infoWrapper, scoreWrapper, matchWrapper);
		wrapper.setId("wrapper");
		wrapper.setClassName("wrapper");
		if (clickedMatch != null) {
			wrapper.getThemeList().add("boxed");
			wrapper.getThemeList().add("intractable");
			wrapper.addClickListener(event -> clickedMatch.onNext(match));
		}
		wrapper.setPadding(false);

		Div predictionWrapper = createPredictionWrapper();
		wrapper.add(predictionWrapper);
		wrapper.getThemeList().add("boxed");

		add(wrapper);
	}

	private Div createPredictionWrapper() {
		predictionButton = new CustomButton("Fill in prediction");
		predictionButton.setId("prediction-button");
		predictionButton.addThemeName("small");
		predictionButton.addPreventClickPropagation();
		predictionButton.addClickListener(ignored -> openPrediction());

		yourPredictionWrapper = new Div();
		yourPredictionWrapper.setId("your-prediction-wrapper");

		H4 pointsGained = new H4(Resources.getMessage("points") + ": " + OverviewUtil.getScoreFormatted(match.getPredictionScore()));
		pointsGained.setVisible(match.getGameHomeTeamScore() != null);

		// Order: prediction label | points | button (button pushed to the right)
		Div predictionWrapper = new Div(yourPredictionWrapper, pointsGained, predictionButton);
		predictionWrapper.setClassName("prediction-wrapper");

		yourPredictionLabel = new PredictionStatusLabel("yourPrediction");
		match.predictionCHanged().subscribe(b -> {
			predictionBean = new MatchPredictionBean(match.getLeague(), match.getPrediction());
			setPredictionStatusText();
		});
		setPredictionStatusText();
		yourPredictionWrapper.add(yourPredictionLabel);

		boolean matchIsEditable = match.getAwayTeam() != null && match.getHomeTeam() != null && nowIsBeforeMatch();
		predictionButton.setVisible(matchIsEditable || isForSuperAdmin);

		return predictionWrapper;
	}

	private Div createMatchWrapper() {
		Div matchWrapper = new Div();
		matchWrapper.setId("match-wrapper");
		Game game = match.getGame();
		// The first knockout round is where group-position placeholders (e.g. 1A, 2B, 3ABCDF) are
		// assigned to real teams by the admin. That round is the Round of 16 for older templates and
		// the Round of 32 for FIFA World Cup 2026. We additionally require the placeholders to map to
		// groups, so later knockout rounds (filled automatically via next_game) never show comboboxes.
		boolean isFirstKnockoutRound = SoccerCupStages.EIGHTH_FINALS.toString().equals(game.getStage()) ||
				SoccerCupStages.ROUND_OF_32.toString().equals(game.getStage());
		boolean placeholdersAreGroupBased =
				(game.getHome_team_placeholder() != null && !GameServiceImpl.getGroups(game.getHome_team_placeholder()).isEmpty()) ||
				(game.getAway_team_placeholder() != null && !GameServiceImpl.getGroups(game.getAway_team_placeholder()).isEmpty());
		boolean isEightFinalAndCanChooseContestant = isFirstKnockoutRound &&
				placeholdersAreGroupBased &&
				DateUtil.nowIsBeforeUtcDateTime(game.getGameDateTime()) &&
				loggedInUserIsAdmin &&
				canAdjustContestants;

		Component left;
		Component right;
		if (isEightFinalAndCanChooseContestant) {
			HorizontalLayout leftH = new HorizontalLayout();
			leftH.setPadding(true);
			leftH.add(getContestantComboBox(game, match.getHomeContestantChanged(), game.getHome_team_placeholder(), game.getHome_team(), game::setHome_team));
			left = leftH;

			HorizontalLayout rightH = new HorizontalLayout();
			rightH.setPadding(true);
			rightH.addClassName("combobox-contestant-selector");
			rightH.add(getContestantComboBox(game, match.getAwayContestantChanged(), game.getAway_team_placeholder(), game.getAway_team(), game::setAway_team));
			right = rightH;
		} else {
			Div homeCard = createTeamCard(match.getHomeTeam(), game.getHome_team_placeholder());
			match.getHomeContestantChanged().subscribe(homeTeam -> {
				if (homeTeam != null) {
					homeCard.removeAll();
					populateTeamCard(homeCard, homeTeam);
				}
			});
			left = homeCard;

			Div awayCard = createTeamCard(match.getAwayTeam(), game.getAway_team_placeholder());
			match.getAwayContestantChanged().subscribe(awayTeam -> {
				if (awayTeam != null) {
					awayCard.removeAll();
					populateTeamCard(awayCard, awayTeam);
				}
			});
			right = awayCard;
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

	private Div createTeamCard(Contestant contestant, String placeholder) {
		Div card = new Div();
		card.addClassName("team-card");
		if (contestant != null) {
			populateTeamCard(card, contestant);
		} else {
			card.add(new Label(placeholder));
		}
		return card;
	}

	private void populateTeamCard(Div card, Contestant contestant) {
		Image flag = new Image(contestant.getIcon_path(), contestant.getName());
		flag.addClassName("team-flag");
		Label name = new Label(contestant.getName());
		name.addClassName("team-name");
		card.add(flag, name);
	}

	private Div createScoreWrapper() {
		CustomButton scoreButton = new CustomButton(isForSuperAdmin ? "Fill in score for all games" : "Fill in score");
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
		H4 date = new H4(DateUtil.formatInUserTimezone(match.getGame().getGameDateTime()));
		date.addClassName("info-date");

		Div infoLeft = new Div(date);
		infoLeft.setClassName("info-left");

		if (showDetails) {
			Span stage = new Span(SoccerCupStages.getLeagueStageTitle(match.getGame(), match.getHomeTeam() != null ? match.getHomeTeam() : match.getAwayTeam()));
			stage.addClassName("info-meta");
			Span place = new Span(match.getGame().getLocation());
			place.addClassName("info-meta");
			infoLeft.add(stage, place);
		}

		Div infoWrapper = new Div(infoLeft, createStatusBadge());
		infoWrapper.setClassName("info-wrapper");
		return infoWrapper;
	}

	private Span createStatusBadge() {
		Span badge = new Span();
		badge.addClassName("status-badge");
		Instant now = Instant.now();
		Instant matchStart = match.getGame().getGameDateTime().toInstant(ZoneOffset.UTC);
		boolean hasScore = match.getGameHomeTeamScore() != null;
		boolean isLive = now.isAfter(matchStart) && now.isBefore(matchStart.plusSeconds(2 * 60 * 60));
		boolean isUpcoming = now.isBefore(matchStart);
		if (hasScore) {
			badge.setText("Finished");
			badge.addClassName("status-badge--finished");
		} else if (isLive) {
			badge.setText("Live");
			badge.addClassName("status-badge--live");
		} else if (isUpcoming) {
			badge.setText("Upcoming");
			badge.addClassName("status-badge--upcoming");
		} else {
			badge.setText("Awaiting score");
			badge.addClassName("status-badge--upcoming");
		}
		return badge;
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
		return DateUtil.nowIsBeforeUtcDateTime(match.getGame().getGameDateTime());
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

		if (isScore || nowIsBeforeMatch() || isForSuperAdmin) {
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
