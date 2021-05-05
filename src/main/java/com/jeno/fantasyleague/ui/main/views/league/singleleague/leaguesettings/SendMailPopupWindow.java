package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;

import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class SendMailPopupWindow {

	private final List<User> leagueUsers;
	private final ApplicationEmailService emailService;

	public SendMailPopupWindow(List<User> leagueUsers, ApplicationEmailService emailService) {
		this.leagueUsers = leagueUsers;
		this.emailService = emailService;
	}

	public void show() {
		new PopupWindow.Builder(
							Resources.getMessage("sendMail"),
						this::createLayout)
				.setHeight(500)
				.setWidth(350)
				.setType(PopupWindow.Type.CONFIRM)
				.setConfirmText(Resources.getMessage("sendMail"))
				.build()
				.open();
	}

	private Component createLayout(PopupWindow window) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		TextField subjectField = new TextField(Resources.getMessage("subject"));
		TextArea body = new TextArea(Resources.getMessage("body"));
		body.setPlaceholder("Write here ...");
		body.setSizeFull();
		window.setOnConfirm(() -> {
			if (subjectField.getValue() == null || subjectField.getValue().isEmpty()) {
				subjectField.setErrorMessage(Resources.getMessage("error.notEmpty"));
				return false;
			} else {
				leagueUsers.forEach(user -> emailService.sendEmail(subjectField.getValue(), body.getValue(), user));
				return true;
			}
		});
		layout.add(subjectField);
		layout.add(body);
		return layout;
	}
}
