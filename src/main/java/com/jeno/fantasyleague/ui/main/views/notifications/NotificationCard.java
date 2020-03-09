package com.jeno.fantasyleague.ui.main.views.notifications;

import java.util.function.Consumer;

import com.jeno.fantasyleague.backend.model.UserNotification;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("notification-card")
@JsModule("./src/views/notification/notification-card.js")
public class NotificationCard extends PolymerTemplate<TemplateModel> {

	private final NotificationBean bean;
	private final Consumer<UserNotification> accepted;
	private final Consumer<UserNotification> declined;

	@Id("content")
	private Div content;

	@Id("wrapper")
	private HorizontalLayout wrapper;

	@Id("notificationMsg")
	private H4 notificationMsg;

	public NotificationCard(NotificationBean bean, Consumer<UserNotification> accepted, Consumer<UserNotification> declined) {
		this.bean = bean;
		this.accepted = accepted;
		this.declined = declined;

		initLayout();
	}

	private void initLayout() {
		wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
		notificationMsg.setText(bean.getNotification().getMessage());
	}

	@ClientCallable
	private void acceptNotification() {
		accepted.accept(bean.getNotification());
	}

	@ClientCallable
	private void declineNotification() {
		declined.accept(bean.getNotification());
	}

}
