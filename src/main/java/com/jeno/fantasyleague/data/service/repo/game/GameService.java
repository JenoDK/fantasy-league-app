package com.jeno.fantasyleague.data.service.repo.game;

import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage.KnockoutGameBean;

import java.util.List;

public interface GameService {

	void updateGroupStageGameScores(List<Game> games);

	void updateKnockoutStageScore(Game game);
}
