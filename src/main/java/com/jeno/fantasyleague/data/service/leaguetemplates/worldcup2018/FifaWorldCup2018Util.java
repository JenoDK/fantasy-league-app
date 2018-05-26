package com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.model.LeagueSetting;

public class FifaWorldCup2018Util {

	private FifaWorldCup2018Util() {}

	public static ArrayListMultimap<FifaWorldCup2018Stages, LeagueSetting> getLeagueSettingsPerStage(List<LeagueSetting> allLeagueSettings) {
		ArrayListMultimap<FifaWorldCup2018Stages, LeagueSetting> leagueSettingPerGroup = ArrayListMultimap.create();
		for (LeagueSetting leagueSetting : allLeagueSettings) {
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
		return leagueSettingPerGroup;
	}
}
