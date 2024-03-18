package com.jeno.fantasyleague.ui.main.views.league.singleleague.chat;

import com.jeno.fantasyleague.backend.model.User;

import java.time.LocalDateTime;

public class ChatMessage {

	private final User user;
	private final String message;
	private final LocalDateTime timeSent;

	public ChatMessage(User user, String message, LocalDateTime timeSent) {
		this.user = user;
		this.message = message;
		this.timeSent = timeSent;
	}

	public User getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTimeSent() {
		return timeSent;
	}

}
