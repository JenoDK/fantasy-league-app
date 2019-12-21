package com.jeno.fantasyleague.ui.main.views.league.singleleague.leaguesettings;

import java.util.List;

import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.window.PopupWindow;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
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
						"sendLeagueMailWindow",
						this::createLayout)
//				.closable(true)
//				.resizable(true)
				.setHeight(500)
				.setWidth(520)
				.build()
				.open();
	}

	private Component createLayout(Dialog window) {
		VerticalLayout layout = new VerticalLayout();
		TextField subjectField = new TextField(Resources.getMessage("subject"));
		RichTextEditor body = new RichTextEditor(Resources.getMessage("body"));
		body.setSizeFull();
		Button sendMail = new CustomButton(Resources.getMessage("sendMail"));
		sendMail.addClickListener(ignored -> {
			if (subjectField.getValue() == null || subjectField.getValue().isEmpty()) {
				subjectField.setErrorMessage(Resources.getMessage("error.notEmpty"));
			} else {
				leagueUsers.forEach(user -> emailService.sendEmail(subjectField.getValue(), body.getValue(), user));
				window.close();
			}
		});
		layout.add(subjectField);
		layout.add(body);
		layout.add(sendMail);
		return layout;
	}
}
