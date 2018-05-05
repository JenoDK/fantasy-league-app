package com.jeno.fantasyleague.ui.main.views.league;

import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.data.service.repo.user.UserService;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.model.enums.NotificationType;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
public class SingleLeagueServiceProvider {

	@Autowired
	private UserService userService;

	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private UserNotificationRepository userNotificationRepository;

	@Autowired
	private SecurityHolder securityHolder;

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

	public ContestantWeightRepository getContestantWeightRepository() {
		return contestantWeightRepository;
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

	public List<User> getUsersWithPendingInvite(League league) {
		return userNotificationRepository
				.findByNotificationTypeAndReferenceIdAndJoinUsers(NotificationType.LEAGUE_INVITE, league.getId()).stream()
						.map(UserNotification::getUser)
						.collect(Collectors.toList());
	}

	public List<ContestantWeight> getContestantWeights(League league) {
		return contestantWeightRepository.findByUserAndLeagueAndJoinContestant(securityHolder.getUser(), league);
	}

	public boolean userIsLeagueAdmin(League league) {
		User loggedInUser = securityHolder.getUser();
		return leagueRepository.fetchLeagueOwners(league.getId()).stream()
				.anyMatch(owner -> owner.getId().equals(loggedInUser.getId()));
	}
}
