package com.jeno.fantasyleague.backend.data.service.notificationtypes;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class NotificationTypeLeagueInviteService implements NotificationTypeService {

	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private UserNotificationRepository userNotificationRepository;

	@Autowired
	private LeagueService leagueService;

	@Override
	public void accepted(UserNotification notification) throws NotificationException {
		Optional<League> league = leagueRepository.findByIdAndJoinUsers(notification.getReference_id());
		if (league.isPresent()) {
			leagueService.addUserToLeague(league.get(), notification.getUser());
		} else {
			throw new NotificationException("League no longer exists");
		}
		setNotificationViewed(notification);
	}

	@Override
	public void declined(UserNotification notification) throws NotificationException {
		setNotificationViewed(notification);
	}

	private void setNotificationViewed(UserNotification notification) {
		notification.setViewed(true);
		userNotificationRepository.saveAndFlush(notification);
	}

}
