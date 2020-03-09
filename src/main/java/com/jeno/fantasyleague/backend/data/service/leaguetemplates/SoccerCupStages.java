package com.jeno.fantasyleague.backend.data.service.leaguetemplates;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.resources.Resources;

public enum SoccerCupStages {

	GROUP_PHASE("groupPhase", 0),
	EIGHTH_FINALS("eighthFinals", 10),
	QUARTER_FINALS("quarterFinals", 20),
	SEMI_FINALS("semiFinals", 30),
	FINALS("finals", 40);

	private final String name;
	private final int seq;

	SoccerCupStages(String name, int seq) {
		this.name = name;
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public int getSeq() {
		return seq;
	}

	public static ArrayListMultimap<SoccerCupStages, LeagueSetting> getLeagueSettingsPerStage(List<LeagueSetting> allLeagueSettings) {
		ArrayListMultimap<SoccerCupStages, LeagueSetting> leagueSettingPerGroup = ArrayListMultimap.create();
		for (LeagueSetting leagueSetting : allLeagueSettings) {
			if (leagueSetting.getName().contains(GROUP_PHASE.getName())) {
				leagueSettingPerGroup.put(GROUP_PHASE, leagueSetting);
			} else if (leagueSetting.getName().contains(EIGHTH_FINALS.getName())) {
				leagueSettingPerGroup.put(EIGHTH_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(QUARTER_FINALS.getName())) {
				leagueSettingPerGroup.put(QUARTER_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(SEMI_FINALS.getName())) {
				leagueSettingPerGroup.put(SEMI_FINALS, leagueSetting);
			} else if (leagueSetting.getName().contains(FINALS.getName())) {
				leagueSettingPerGroup.put(FINALS, leagueSetting);
			}
		}
		return leagueSettingPerGroup;
	}

	public static String getLeagueStageTitle(Game game, Contestant team) {
		SoccerCupStages stage = SoccerCupStages.valueOf(game.getStage());
		switch (stage) {
			case GROUP_PHASE:
				return team.getContestant_group().getName() + " - stage " + game.getRound();
			case EIGHTH_FINALS:
				return Resources.getMessage("roundOf16");
			case QUARTER_FINALS:
				return Resources.getMessage("quarterFinals");
			case SEMI_FINALS:
				return Resources.getMessage("semiFinals");
			case FINALS:
				return Resources.getMessage("finals");
		}
		throw new RuntimeException("Impossible");
	}
}
