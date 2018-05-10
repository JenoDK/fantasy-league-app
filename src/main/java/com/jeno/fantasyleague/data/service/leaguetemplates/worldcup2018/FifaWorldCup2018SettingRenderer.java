package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.jeno.fantasyleague.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class FifaWorldCup2018SettingRenderer implements LeagueSettingRenderer {

	public static final String ALL_CORRECT = "AllCorrect";
	public static final String WRONG_SCORE = "WrongScore";
	public static final String ALL_WRONG = "AllWrong";

	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	@Override
	public Component render(League league) {
		Accordion layout = new Accordion();

		ArrayListMultimap<FifaWorldCup2018Stages, LeagueSetting> leagueSettingPerGroup = ArrayListMultimap.create();
		for (LeagueSetting leagueSetting : leagueSettingRepository.findByLeague(league)) {
			if (leagueSetting.getName().contains(FifaWorldCup2018Stages.GROUP_PHASE.getName())) {
				leagueSettingPerGroup.put(FifaWorldCup2018Stages.GROUP_PHASE, leagueSetting);
			} else if (leagueSetting.getName().contains(FifaWorldCup2018Stages.EIGHTH_FINALS.getName())) {
				leagueSettingPerGroup.put(FifaWorldCup2018Stages.EIGHTH_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(FifaWorldCup2018Stages.QUARTER_FINALS.getName())) {
				leagueSettingPerGroup.put(FifaWorldCup2018Stages.QUARTER_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(FifaWorldCup2018Stages.SEMI_FINALS.getName())) {
				leagueSettingPerGroup.put(FifaWorldCup2018Stages.SEMI_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(FifaWorldCup2018Stages.FINALS.getName())) {
				leagueSettingPerGroup.put(FifaWorldCup2018Stages.FINALS, leagueSetting);
			}
		}
		leagueSettingPerGroup.keySet().stream()
				.sorted(Comparator.comparing(FifaWorldCup2018Stages::getSeq))
				.forEach(stage -> {
					List<IntegerSettingsBean> leagueSettings = leagueSettingPerGroup.get(stage).stream()
							.map(IntegerSettingsBean::new)
							.collect(Collectors.toList());
					SettingsGroupLayout settingsGroupLayout = new SettingsGroupLayout(stage.getName(), leagueSettings);
					settingsGroupLayout.saved().subscribe(leagueSettingRepository::saveAll);
					layout.addTab(settingsGroupLayout, Resources.getMessage(stage.getName()));
				});
		return layout;
	}

	public void addDefaultLeagueSettings(League newLeague) {
		List<LeagueSetting> labSettings = Lists.newArrayList();

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + ALL_CORRECT, 3));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + WRONG_SCORE, 2));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.GROUP_PHASE.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + ALL_CORRECT, 6));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + WRONG_SCORE, 4));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.EIGHTH_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + ALL_CORRECT, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + WRONG_SCORE, 7));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.QUARTER_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + ALL_CORRECT, 15));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + WRONG_SCORE, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.SEMI_FINALS.getName() + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + ALL_CORRECT, 30));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + WRONG_SCORE, 20));
		labSettings.add(createLeagueScoreSettings(newLeague, FifaWorldCup2018Stages.FINALS.getName() + ALL_WRONG, 0));

		leagueSettingRepository.saveAll(labSettings);
	}

	private LeagueSetting createLeagueScoreSettings(League newLeague, String name, int value) {
		LeagueSetting correctResultAndScorePoints = new LeagueSetting();
		correctResultAndScorePoints.setName(name);
		correctResultAndScorePoints.setValue(Integer.valueOf(value).toString());
		correctResultAndScorePoints.setLeague(newLeague);
		return correctResultAndScorePoints;
	}

}
