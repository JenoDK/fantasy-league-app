package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.service.repo.user.UserService;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class SingleLeagueServiceProvider {

	@Autowired
	private UserService userService;

	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private LeagueRepository leagueRepository;

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	public LeagueRepository getLeagueRepository() {
		return leagueRepository;
	}

	public UserService getUserService() {
		return userService;
	}

	public ContestantGroupRepository getContestantGroupRepository() {
		return contestantGroupRepository;
	}
}
