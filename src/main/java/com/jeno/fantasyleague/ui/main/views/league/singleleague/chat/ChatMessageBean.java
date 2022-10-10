package com.jeno.fantasyleague.ui.main.views.league.singleleague.chat;

public class ChatMessageBean {

	private final String username;
	private final String message;
	private final String timeSent;
	private final Boolean isFromLoggedInUser;

	public ChatMessageBean(String username, String message, String timeSent, Boolean isFromLoggedInUser) {
		this.username = username;
		this.message = message;
		this.timeSent = timeSent;
		this.isFromLoggedInUser = isFromLoggedInUser;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public String getTimeSent() {
		return timeSent;
	}

	public Boolean getIsFromLoggedInUser() {
		return isFromLoggedInUser;
	}
}
