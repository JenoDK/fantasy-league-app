package com.jeno.fantasyleague.backend.data.service.repo.game;

import com.jeno.fantasyleague.backend.model.Game;

import java.util.List;

public interface GameService {

	void updateGroupStageGameScores(Game game);

	Game updateKnockoutStageScore(Game game);
}
