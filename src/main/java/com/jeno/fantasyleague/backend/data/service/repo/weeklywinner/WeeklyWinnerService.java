package com.jeno.fantasyleague.backend.data.service.repo.weeklywinner;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.WeeklyWinner;

import java.util.Optional;

public interface WeeklyWinnerService {

	/**
	 * Snapshot the current leader(s) of every active World Cup 2026 league and store them as this
	 * week's winner. Triggered on a schedule, but exposed so it can be invoked manually.
	 */
	void recordWeeklyWinners();

	/**
	 * The most recent weekly-winner announcement for the league that the given member has not seen yet,
	 * or empty if there is none / it was already seen.
	 */
	Optional<WeeklyWinner> getLatestUnseen(League league, LeagueUser leagueUser);

}
