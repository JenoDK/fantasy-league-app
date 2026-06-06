package com.jeno.fantasyleague.ui.main.views.league.singleleague.users;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.grid.CustomGrid;
import com.jeno.fantasyleague.ui.common.grid.CustomGridBuilder;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.LayoutUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Collections;
import java.util.List;

public class UserGrid extends CustomGrid<User> {

	public UserGrid(ListDataProvider<User> dataProvider, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		super(getDefaultUserGridBuilder(dataProvider));
	}

	public static Button promoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		Button promoteDemoteButton = new CustomButton();
		changePromoteDemoteButton(user, singleLeagueServiceProvider, league, promoteDemoteButton);
		promoteDemoteButton.addClickListener(ignored -> {
			if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
				singleLeagueServiceProvider.demoteUserToLeagueNonOwner(league, user);
			} else {
				singleLeagueServiceProvider.promoteUserToLeagueOwner(league, user);
			}
			changePromoteDemoteButton(user, singleLeagueServiceProvider, league, promoteDemoteButton);
		});
		if (league.getCreatedBy().getId().equals(user.getId())) {
			promoteDemoteButton.setVisible(false);
		}
		return promoteDemoteButton;
	}

	private static final String DEFAULT_INVITE_SUBJECT = "FIFA World Cup 2026 - League Invite";

	private static String defaultInviteBody(League league) {
		return "You got invited to participate in the league " + league.getName()
				+ ". Log in to https://jenodk.com/fantasy-league and accept the invite.";
	}

	public static Button sendMailButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		Button sendMailButton = new CustomButton(VaadinIcon.MAILBOX);
		sendMailButton.addClickListener(ignored ->
				openInviteEmailDialog(Collections.singletonList(user), singleLeagueServiceProvider, league));
		return sendMailButton;
	}

	/**
	 * Opens a dialog pre-filled with the default invite subject/body, lets the admin edit it, and on
	 * confirm sends that (possibly edited) message to every given recipient.
	 */
	public static void openInviteEmailDialog(List<User> recipients, SingleLeagueServiceProvider singleLeagueServiceProvider, League league) {
		if (recipients.isEmpty()) {
			Notification.show("There are no pending invites to send to");
			return;
		}
		Dialog dialog = new Dialog();
		dialog.setWidth("500px");

		Span recipientInfo = new Span(recipients.size() == 1
				? "Sending to " + recipients.get(0).getEmail()
				: "Sending to " + recipients.size() + " pending users");

		TextField subjectField = new TextField("Subject");
		subjectField.setWidthFull();
		subjectField.setValue(DEFAULT_INVITE_SUBJECT);

		TextArea bodyField = new TextArea("Message");
		bodyField.setWidthFull();
		bodyField.setHeight("160px");
		bodyField.setValue(defaultInviteBody(league));

		Button sendButton = new CustomButton("Send", VaadinIcon.PAPERPLANE.create());
		sendButton.addClickListener(ignored -> {
			recipients.forEach(user -> sendInviteEmail(user, singleLeagueServiceProvider, subjectField.getValue(), bodyField.getValue()));
			dialog.close();
			Notification.show("Sent invite email to " + recipients.size() + " user(s)");
		});
		Button cancelButton = new CustomButton("Cancel");
		cancelButton.addClickListener(ignored -> dialog.close());

		dialog.add(new VerticalLayout(recipientInfo, subjectField, bodyField, new HorizontalLayout(sendButton, cancelButton)));
		dialog.open();
	}

	public static void sendInviteEmail(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, String subject, String body) {
		try {
			singleLeagueServiceProvider.getEmailService().sendEmail(subject, body, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void changePromoteDemoteButton(User user, SingleLeagueServiceProvider singleLeagueServiceProvider, League league, Button promoteButton) {
		if (singleLeagueServiceProvider.userIsLeagueAdmin(league, user)) {
			promoteButton.setText("Demote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());
		} else {
			promoteButton.setText("Promote");
			promoteButton.setIcon(VaadinIcon.ARROW_CIRCLE_UP.create());
		}
	}

	public static CustomGridBuilder getDefaultUserGridBuilder(DataProvider<User, ?> dataProvider) {
		return new CustomGridBuilder<>(dataProvider, User::getId)
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"usernameColumn",
								User::getUsername,
								"Username"))
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"nameColumn",
								User::getName,
								"Name"))
				.withTextColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"emailColumn",
								User::getEmail,
								"Email"))
				.withIconColumn(
						new CustomGridBuilder.ColumnProvider<>(
								"iconColumn",
								LayoutUtil::getUserIconColumnValue,
								""))
				.withColumnOrder("iconColumn", "usernameColumn", "nameColumn", "emailColumn");
	}

}
