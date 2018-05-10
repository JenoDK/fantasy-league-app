package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;

import com.jeno.fantasyleague.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

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
				"sendLeagueMailWindow",
				window -> createLayout(window))
				.closable(true)
				.resizable(true)
				.setHeight(500)
				.setWidth(520)
				.build()
				.show();
	}

	private Component createLayout(Window window) {
		VerticalLayout layout = new VerticalLayout();
		TextField subjectField = new TextField(Resources.getMessage("subject"));
		RichTextArea body = new RichTextArea(Resources.getMessage("body"));
		body.setSizeFull();
		body.setResponsive(true);
		Button sendMail = new Button(Resources.getMessage("sendMail"));
		sendMail.addStyleName(ValoTheme.BUTTON_PRIMARY);
		sendMail.addStyleName(ValoTheme.BUTTON_TINY);
		sendMail.addClickListener(ignored -> {
			if (subjectField.getValue() == null || subjectField.getValue().isEmpty()) {
				subjectField.setComponentError(new UserError(Resources.getMessage("error.notEmpty")));
			} else {
				leagueUsers.forEach(user -> emailService.sendEmail(subjectField.getValue(), body.getValue(), user));
				window.close();
			}
		});
		layout.addComponent(subjectField);
		layout.addComponent(body);
		layout.addComponent(sendMail);
		return layout;
	}
}
