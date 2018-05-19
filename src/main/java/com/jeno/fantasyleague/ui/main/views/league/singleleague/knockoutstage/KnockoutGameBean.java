package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;

public class KnockoutGameBean {

	private Game game;
	private Contestant contestant2;
	private Contestant contestant1;

	public KnockoutGameBean(Game game, Contestant contestant1, Contestant contestant2) {
		this.game = game;
		this.contestant1 = contestant1;
		this.contestant2 = contestant2;
	}

	public Contestant getContestant2() {
		return contestant2;
	}

	public void setContestant2(Contestant contestant2) {
		this.contestant2 = contestant2;
		game.setAway_team(contestant2);
	}

	public Contestant getContestant1() {
		return contestant1;
	}

	public void setContestant1(Contestant contestant1) {
		this.contestant1 = contestant1;
		game.setHome_team(contestant1);
	}

	public Game getGame() {
		return game;
	}
}
