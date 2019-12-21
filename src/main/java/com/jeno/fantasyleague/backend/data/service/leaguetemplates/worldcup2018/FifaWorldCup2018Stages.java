package com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018;

public enum FifaWorldCup2018Stages {

	GROUP_PHASE("groupPhase", 0),
	EIGHTH_FINALS("eighthFinals", 10),
	QUARTER_FINALS("quarterFinals", 20),
	SEMI_FINALS("semiFinals", 30),
	FINALS("finals", 40);

	private final String name;
	private final int seq;

	FifaWorldCup2018Stages(String name, int seq) {
		this.name = name;
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public int getSeq() {
		return seq;
	}
}
