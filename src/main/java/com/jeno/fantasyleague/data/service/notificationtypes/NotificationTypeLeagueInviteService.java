package com.jeno.fantasyleague.data.service.notificationtypes;

import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.data.service.repo.contestantweight.ContestantWeightService;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class NotificationTypeLeagueInviteService implements NotificationTypeService {

	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private UserNotificationRepository userNotificationRepository;

	@Autowired
	private ContestantWeightService contestantWeightService;

	@Override
	public void accepted(UserNotification notification) throws NotificationException {
		Optional<League> league = leagueRepository.findByIdAndJoinUsers(notification.getReference_id());
		if (league.isPresent()) {
			league.get().getUsers().add(notification.getUser());
			leagueRepository.saveAndFlush(league.get());

			contestantWeightService.addDefaultContestantWeights(league.get(), notification.getUser());
		} else {
			throw new NotificationException("League no longer exists");
		}
		notification.setViewed(true);
		userNotificationRepository.saveAndFlush(notification);
	}

	@Override
	public void declined(UserNotification notification) throws NotificationException {

	}

}
