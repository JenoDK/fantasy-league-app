package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018SettingRenderer;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Util;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class FaqTab extends VerticalLayout {

	public static final String SCORE_CALCULATION = "%s * (1 + %s/100) + (1/(%s/100)) * %s";

	private final League league;
	private final SingleLeagueServiceProvider singleLeagueServiceProvider;

	private Accordion questionsAccordion;

	public FaqTab(League league, SingleLeagueServiceProvider singleLeagueServiceProvider) {
		super();
		this.league = league;
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;

		questionsAccordion = new Accordion();
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
		addComponent(questionsAccordion);
	}

	private void addQuestion(String question, String answer, Component...afterAnswerComponents) {
		VerticalLayout answerLayout = new VerticalLayout();
		Label answerLabel = new Label(answer, ContentMode.HTML);
		answerLayout.addComponent(answerLabel);
		Arrays.stream(afterAnswerComponents).forEach(answerLayout::addComponents);
		questionsAccordion.addTab(answerLayout, question);
	}

	private VerticalLayout createCalculationLabel() {
		VerticalLayout calcLayout = new VerticalLayout();
		calcLayout.setMargin(false);

		calcLayout.addComponent(
				new Label("Earned points = " + String.format(SCORE_CALCULATION, "r", "w", "p", "q"), ContentMode.HTML));
		calcLayout.addComponent(new Label("r: " + Resources.getMessage("matchResultFromTableBelow")));
		calcLayout.addComponent(new Label("w: " + Resources.getMessage("teamWeight")));
		calcLayout.addComponent(new Label("p: " + Resources.getMessage("teamPowerIndex")));
		calcLayout.addComponent(new Label("q: " + Resources.getMessage("modifierForPowerIndex", findCurrentPowerIndexMultiplier())));

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
}
