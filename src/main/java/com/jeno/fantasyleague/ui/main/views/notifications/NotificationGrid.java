package com.jeno.fantasyleague.ui.main.views.notifications;

import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import io.reactivex.subjects.BehaviorSubject;

public class NotificationGrid extends Grid<NotificationBean> {

	private final List<UserNotification> notifications;
	private final NotificationModel notificationModel;
	private final SecurityHolder securityHolder;
	private final BehaviorSubject<UserNotification> leagueAccepted;

	public NotificationGrid(List<UserNotification> notifications, SecurityHolder securityHolder, NotificationModel notificationModel, BehaviorSubject<UserNotification> leagueAccepted) {
		this.notifications = notifications;
		this.securityHolder = securityHolder;
		this.notificationModel = notificationModel;
		this.leagueAccepted = leagueAccepted;

		initLayout();
	}

	private void initLayout() {
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(true);
		addThemeNames("card-grid", "no-row-borders");
		getStyle().set("border", "none");

		addColumn(new ComponentRenderer<>(notification -> new NotificationCard(
				notification,
				this::accepted,
				this::declined)));
		setNotificatins(notifications);
	}

	private void declined(UserNotification notification) {
		notificationModel.notificationDeclined(notification);
		setNotificatins(securityHolder.getUserNotifications());
	}

	private void accepted(UserNotification notification) {
		notificationModel.notificationAccepted(notification);
		setNotificatins(securityHolder.getUserNotifications());
		leagueAccepted.onNext(notification);
	}

	private void setNotificatins(List<UserNotification> userNotifications) {
		setItems(userNotifications.stream()
				.map(NotificationBean::new)
				.collect(Collectors.toList()));
	}
}
