package com.jeno.fantasyleague.backend.data.service.repo.leaguemessage;

import java.util.List;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueMessage;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;

public interface LeagueMessageService {

	LeagueMessage addLeagueMessage(League league, String message, User user);

	void resetUnreadMessages(LeagueUser loggedInLeagueUser);

	List<LeagueMessage> getLeagueMessages(League league, User loggedInUser);
}
