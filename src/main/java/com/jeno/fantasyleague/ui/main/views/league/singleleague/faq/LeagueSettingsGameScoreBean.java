package com.jeno.fantasyleague.ui.main.views.league.singleleague.faq;

public class LeagueSettingsGameScoreBean {

	private final String stage;
	private final Integer allCorrect;
	private final Integer correctResultAndWrongScore;
	private final Integer allWrong;

	public LeagueSettingsGameScoreBean(String stage, Integer allCorrect, Integer correctResultAndWrongScore, Integer allWrong) {
		this.stage = stage;
		this.allCorrect = allCorrect;
		this.correctResultAndWrongScore = correctResultAndWrongScore;
		this.allWrong = allWrong;
	}

	public String getStage() {
		return stage;
	}

	public Integer getAllCorrect() {
		return allCorrect;
	}

	public Integer getCorrectResultAndWrongScore() {
		return correctResultAndWrongScore;
	}

	public Integer getAllWrong() {
		return allWrong;
	}
}
