package com.jeno.fantasyleague.backend.data.service.repo.leaguemessage;

import com.jeno.fantasyleague.backend.data.repository.LeagueMessageRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueMessage;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class LeagueMessageServiceImpl implements LeagueMessageService {

	@Autowired
	private LeagueMessageRepository leagueMessageRepository;
	@Autowired
	private LeagueUserRepository leagueUserRepository;

	@Override
	@Async
	public LeagueMessage addLeagueMessage(League league, String message, User user) {
		LeagueMessage leagueMessage = new LeagueMessage();
		leagueMessage.setLeague(league);
		leagueMessage.setMessage(message);
		LeagueMessage addedLeagueMessage = leagueMessageRepository.saveAndFlush(leagueMessage);
		leagueUserRepository.findByLeague(league).stream()
				.filter(lu -> !user.getId().equals(lu.getUser().getId()))
				.forEach(lu -> lu.setUnread_messages(lu.getUnread_messages() + 1));
		return addedLeagueMessage;
	}

	@Override
	public void resetUnreadMessages(LeagueUser loggedInLeagueUser) {
		loggedInLeagueUser.setUnread_messages(0);
		leagueUserRepository.saveAndFlush(loggedInLeagueUser);
	}

	@Override
	public List<LeagueMessage> getLeagueMessages(League league, User loggedInUser) {
		return leagueMessageRepository.findByLeague(league);
	}

}
