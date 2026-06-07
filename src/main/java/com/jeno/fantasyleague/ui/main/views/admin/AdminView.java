package com.jeno.fantasyleague.ui.main.views.admin;

import com.jeno.fantasyleague.backend.data.service.repo.reminder.ReminderEmailService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class AdminView {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;
	private final ReminderEmailService reminderEmailService;

	private boolean init = false;

	private VerticalLayout rootLayout;
	private Tabs tabs;

	private AdminTabs adminTabs;

	@Autowired
	public AdminView(SingleLeagueServiceProvider singleLeagueServiceProvider, ReminderEmailService reminderEmailService) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.reminderEmailService = reminderEmailService;
	}

	private void initLayout(Optional<League> optionalLeague) {
		rootLayout = new VerticalLayout();
		rootLayout.setPadding(false);
		rootLayout.setSpacing(false);
		rootLayout.setMaxWidth("1200px");
		rootLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);

		adminTabs = new AdminTabs(optionalLeague, singleLeagueServiceProvider, main);

		rootLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		rootLayout.add(createReminderTriggers(), adminTabs, main);

		init = true;
	}

	private HorizontalLayout createReminderTriggers() {
		CustomButton stockReminders = new CustomButton("Send stock reminders", VaadinIcon.BELL.create());
		stockReminders.addClickListener(event -> runReminders("Stock reminders", reminderEmailService::sendStockReminders));
		CustomButton predictionReminders = new CustomButton("Send prediction reminders", VaadinIcon.BELL.create());
		predictionReminders.addClickListener(event -> runReminders("Prediction reminders", reminderEmailService::sendPredictionReminders));

		HorizontalLayout triggers = new HorizontalLayout(stockReminders, predictionReminders);
		triggers.setPadding(true);
		return triggers;
	}

	private void runReminders(String label, Runnable reminderJob) {
		try {
			reminderJob.run();
			Notification.show(label + " sent");
		} catch (Exception e) {
			Notification.show(label + " failed: " + e.getMessage());
		}
	}

	public VerticalLayout getLayout(Optional<League> optionalLeague) {
		if (!init) {
			initLayout(optionalLeague);
		}
		return rootLayout;
	}

}
