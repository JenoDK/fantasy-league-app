package com.jeno.fantasyleague.data.service.repo.game;

import com.jeno.fantasyleague.data.repository.ContestantRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private ContestantRepository contestantRepository;

	@Override
	public void updateGroupStageGameScores(List<Game> games) {
		gameRepository.saveAll(games);
		games.stream().forEach(this::distributePoints);
	}

	private void distributePoints(Game game) {
		Contestant homeTeam = game.getHome_team();
		Integer homeScore = game.getHome_team_score();
		Contestant awayTeam = game.getAway_team();
		Integer awayScore = game.getAway_team_score();
		// Home won
		if (homeScore > awayScore) {
			homeTeam.setPoints_in_group(homeTeam.getPoints_in_group() + 3);
			contestantRepository.saveAndFlush(homeTeam);
		// Draw
		} else if (homeScore == awayScore) {
			homeTeam.setPoints_in_group(homeTeam.getPoints_in_group() + 1);
			awayTeam.setPoints_in_group(homeTeam.getPoints_in_group() + 1);
			contestantRepository.saveAndFlush(homeTeam);
			contestantRepository.saveAndFlush(awayTeam);
		// Away won
		} else {
			awayTeam.setPoints_in_group(homeTeam.getPoints_in_group() + 3);
			contestantRepository.saveAndFlush(awayTeam);
		}
	}
}
