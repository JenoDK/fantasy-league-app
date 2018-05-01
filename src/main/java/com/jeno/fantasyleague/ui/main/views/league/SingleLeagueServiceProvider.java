package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.data.service.repo.user.UserService;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.model.enums.NotificationType;
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
	@Autowired
	private UserNotificationRepository userNotificationRepository;

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

	public UserNotification createLeagueInviteUserNotification(User user, League league) {
		UserNotification notification = new UserNotification();
		notification.setUser(user);
		notification.setMessage(league.getName() + " League invite");
		notification.setReference_id(league.getId());
		notification.setReference_table("league");
		notification.setNotification_type(NotificationType.LEAGUE_INVITE);
		return userNotificationRepository.saveAndFlush(notification);
	}
}
