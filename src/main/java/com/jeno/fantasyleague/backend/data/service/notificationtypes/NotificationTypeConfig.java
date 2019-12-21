package com.jeno.fantasyleague.backend.data.service.notificationtypes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationTypeConfig {

	public static final String LEAGUE_INVITE = "NotificationTypeLeagueInviteService";

	@Bean(name = LEAGUE_INVITE)
	public NotificationTypeLeagueInviteService getNotificationTypeLeagueInviteService() {
		return new NotificationTypeLeagueInviteService();
	}

}
