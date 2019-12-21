package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018SettingRenderer;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Util;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class FaqTab extends VerticalLayout {

	public static final String SCORE_CALCULATION = "%s * (1 + %s/10)";

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceProvider;

	private Accordion questionsAccordion;

	public FaqTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		this.league = league;
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;

		questionsAccordion = new Accordion();
		questionsAccordion.add(
				"How do I play?",
				getHowToPlayComponent());
		addQuestion(
				Resources.getMessage("faq.question.fillInPredictions"),
				Resources.getMessage("faq.answer.fillInPredictions", DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date())));
		addQuestion(
				Resources.getMessage("faq.question.scoreCalculation"),
				Resources.getMessage("faq.answer.scoreCalculation"),
				createCalculationLabel(),
				new LeagueSettingsGameScoresGrid(getSettingsBeans()));
		addQuestion(
				Resources.getMessage("faq.question.leagueProgression"),
				Resources.getMessage("faq.answer.leagueProgression"));
		add(questionsAccordion);
	}

	private void addQuestion(String question, String answer, Component...afterAnswerComponents) {
		VerticalLayout answerLayout = new VerticalLayout();
		Label answerLabel = new Label(answer);
		answerLayout.add(answerLabel);
		Arrays.stream(afterAnswerComponents).forEach(answerLayout::add);
		questionsAccordion.add(question, answerLayout);
	}

	private VerticalLayout createCalculationLabel() {
		VerticalLayout calcLayout = new VerticalLayout();
		calcLayout.setMargin(false);

		calcLayout.add(
				new Label("Earned points = " + String.format(SCORE_CALCULATION, "r", "s")));
		calcLayout.add(new Label("r: " + Resources.getMessage("matchResultFromTableBelow")));
		calcLayout.add(new Label("s: " + Resources.getMessage("stocksPurchased")));
		calcLayout.add(new Label(Resources.getMessage("scoreCalculationExample")));

		return calcLayout;
	}

	public String findCurrentPowerIndexMultiplier() {
		return singleLeagueServiceProvider.getLeagueSettingRepository().findByLeagueAndName(league, FifaWorldCup2018SettingRenderer.POWER_INDEX_MULTIPLIER)
				.map(LeagueSetting::getValue)
				.orElse("");
	}

	public List<LeagueSettingsGameScoreBean> getSettingsBeans() {
		ArrayListMultimap<FifaWorldCup2018Stages, LeagueSetting> leagueSettingPerGroup =
				FifaWorldCup2018Util.getLeagueSettingsPerStage(
						singleLeagueServiceProvider.getLeagueSettingRepository().findByLeague(league));
		return leagueSettingPerGroup.asMap().keySet().stream()
				.sorted(Comparator.comparingInt(FifaWorldCup2018Stages::getSeq))
				.map(stage -> {
					Map<String, LeagueSetting> settingsToNameMap = leagueSettingPerGroup.get(stage).stream()
							.collect(Collectors.toMap(LeagueSetting::getName, Function.identity()));
					Integer allCorrect = Integer.valueOf(
							settingsToNameMap.get(stage.getName() + FifaWorldCup2018SettingRenderer.ALL_CORRECT).getValue());
					Integer wrongScore = Integer.valueOf(
							settingsToNameMap.get(stage.getName() + FifaWorldCup2018SettingRenderer.WRONG_SCORE).getValue());
					Integer allWrong = Integer.valueOf(
							settingsToNameMap.get(stage.getName() + FifaWorldCup2018SettingRenderer.ALL_WRONG).getValue());
					return new LeagueSettingsGameScoreBean(
							Resources.getMessage(stage.getName()),
							allCorrect,
							wrongScore,
							allWrong);
				})
				.collect(Collectors.toList());
	}

	public VerticalLayout getHowToPlayComponent() {
		VerticalLayout layout = new VerticalLayout();

		Label investTitle = new Label("Invest in teams!");
//		investTitle.addClassName(ValoTheme.LABEL_H3);
		layout.add(investTitle);
		layout.add(new Label(
				"Purchase team stocks in the 'Purchase stocks' tab<br/>" +
				"Watch out, stocks of 'better' teams cost more"));

		Label fillInPredictions = new Label("Fill in predictions");
//		fillInPredictions.addClassName(ValoTheme.LABEL_H3);
		layout.add(fillInPredictions);
		layout.add(new Label(
				"Fill in your predictions for the group phase.<br/>" +
				"As for the knockout phase you'll need to wait for the league admins to assign the correct group winner"));

		Label overview = new Label("Overview section");
//		overview.addClassName(ValoTheme.LABEL_H3);
		layout.add(overview);
		layout.add(new Label(
				"You can only view each others predictions once the predictions are locked.<br/>" +
				"You can click each user to see more details and click each game (button in last column)" +
				" to see the results of everyone for that game."));
		return layout;
	}
}
