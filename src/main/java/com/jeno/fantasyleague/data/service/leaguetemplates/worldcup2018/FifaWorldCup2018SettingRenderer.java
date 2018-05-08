package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class FifaWorldCup2018SettingRenderer implements LeagueSettingRenderer {

	public static final String ALL_CORRECT = "AllCorrect";
	public static final String WRONG_SCORE = "WrongScore";
	public static final String ALL_WRONG = "AllWrong";

	public static final String GROUP_PHASE = "groupPhase";
	public static final String EIGHTH_FINALS = "eighthFinals";
	public static final String QUARTER_FINALS = "quarterFinals";
	public static final String SEMI_FINALS = "semiFinals";
	public static final String FINALS = "finals";

	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	@Override
	public Component render(League league) {
		Accordion layout = new Accordion();

		ArrayListMultimap<String, LeagueSetting> leagueSettingPerGroup = ArrayListMultimap.create();
		for (LeagueSetting leagueSetting : leagueSettingRepository.findByLeague(league)) {
			if (leagueSetting.getName().contains(GROUP_PHASE)) {
				leagueSettingPerGroup.put(GROUP_PHASE, leagueSetting);
			} else if (leagueSetting.getName().contains(EIGHTH_FINALS)) {
				leagueSettingPerGroup.put(EIGHTH_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(QUARTER_FINALS)) {
				leagueSettingPerGroup.put(QUARTER_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(SEMI_FINALS)) {
				leagueSettingPerGroup.put(SEMI_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(FINALS)) {
				leagueSettingPerGroup.put(FINALS, leagueSetting);
			}
		}
		leagueSettingPerGroup.keySet().stream()
				.sorted(Comparator.comparing(this::getLeagueSettingGroupIndex))
				.forEach(leagueSettingGroup -> {
					List<IntegerSettingsBean> leagueSettings = leagueSettingPerGroup.get(leagueSettingGroup).stream()
							.map(IntegerSettingsBean::new)
							.collect(Collectors.toList());
					SettingsGroupLayout settingsGroupLayout = new SettingsGroupLayout(leagueSettingGroup, leagueSettings);
					settingsGroupLayout.saved().subscribe(leagueSettingRepository::saveAll);
					layout.addTab(settingsGroupLayout, Resources.getMessage(leagueSettingGroup));
				});
		return layout;
	}

	private int getLeagueSettingGroupIndex(String leagueSettingGroup) {
		if (leagueSettingGroup.contains(GROUP_PHASE)) {
			return 0;
		} else if (leagueSettingGroup.contains(EIGHTH_FINALS)) {
			return 10;
		} else if (leagueSettingGroup.contains(QUARTER_FINALS)) {
			return 20;
		} else if (leagueSettingGroup.contains(SEMI_FINALS)) {
			return 30;
		} else if (leagueSettingGroup.contains(FINALS)) {
			return 40;
		}
		return 0;
	}

	public void addDefaultLeagueSettings(League newLeague) {
		List<LeagueSetting> labSettings = Lists.newArrayList();

		labSettings.add(createLeagueScoreSettings(newLeague, GROUP_PHASE + ALL_CORRECT, 3));
		labSettings.add(createLeagueScoreSettings(newLeague, GROUP_PHASE + WRONG_SCORE, 2));
		labSettings.add(createLeagueScoreSettings(newLeague, GROUP_PHASE + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, EIGHTH_FINALS + ALL_CORRECT, 6));
		labSettings.add(createLeagueScoreSettings(newLeague, EIGHTH_FINALS + WRONG_SCORE, 4));
		labSettings.add(createLeagueScoreSettings(newLeague, EIGHTH_FINALS + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, QUARTER_FINALS+ ALL_CORRECT, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, QUARTER_FINALS + WRONG_SCORE, 7));
		labSettings.add(createLeagueScoreSettings(newLeague, QUARTER_FINALS + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, SEMI_FINALS + ALL_CORRECT, 15));
		labSettings.add(createLeagueScoreSettings(newLeague, SEMI_FINALS + WRONG_SCORE, 10));
		labSettings.add(createLeagueScoreSettings(newLeague, SEMI_FINALS + ALL_WRONG, 0));

		labSettings.add(createLeagueScoreSettings(newLeague, FINALS + ALL_CORRECT, 30));
		labSettings.add(createLeagueScoreSettings(newLeague, FINALS + WRONG_SCORE, 20));
		labSettings.add(createLeagueScoreSettings(newLeague, FINALS + ALL_WRONG, 0));

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
