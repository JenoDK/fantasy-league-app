package com.jeno.fantasyleague.data.service.repo.game;

import com.jeno.fantasyleague.model.Game;

import java.util.List;

public interface GameService {

	void updateGroupStageGameScores(List<Game> games);

	Game updateKnockoutStageScore(Game game);
}
