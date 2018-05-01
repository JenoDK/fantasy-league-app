package com.jeno.fantasyleague.data.service.notificationtypes;

public class NotificationException extends RuntimeException {
	public NotificationException() {
	}

	public NotificationException(String message) {
		super(message);
	}

	public NotificationException(String message, Throwable cause) {
		super(message, cause);
	}
}
