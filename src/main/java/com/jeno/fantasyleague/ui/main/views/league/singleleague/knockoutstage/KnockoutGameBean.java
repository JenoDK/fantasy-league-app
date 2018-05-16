package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;

public class KnockoutGameBean {

	private final Contestant contestant2;
	private final Contestant contestant1;
	private final Game game;

	public KnockoutGameBean(Game game, Contestant contestant1, Contestant contestant2) {
		this.game = game;
		this.contestant1 = contestant1;
		this.contestant2 = contestant2;
	}

	public Contestant getContestant2() {
		return contestant2;
	}

	public Contestant getContestant1() {
		return contestant1;
	}

	public Game getGame() {
		return game;
	}
}
