package com.jeno.fantasyleague.ui.main;

import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.ui.main.broadcast.Broadcaster;
import com.jeno.fantasyleague.ui.main.navigation.TopBar;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

@Theme(Lumo.class)
@PageTitle("Fantasy League")
@Route("main")
@Push(value = PushMode.AUTOMATIC, transport = Transport.LONG_POLLING)
public class MainUI extends VerticalLayout implements Broadcaster.BroadcastListener {

	@Autowired
	private SecurityHolder securityHolder;
	@Autowired
	private NotificationModel notificationModel;

	private TopBar topBar;

	public MainUI() {
		addClassName("main-layout");
		setSpacing(false);
		setMargin(false);
		setSizeFull();

		topBar = new TopBar(securityHolder.getUser(), securityHolder.getUserNotifications(), notificationModel);
		add(topBar);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		// Register to receive broadcasts
		Broadcaster.register(securityHolder.getUser().getId(), this);
		super.onAttach(attachEvent);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		Broadcaster.unregister(securityHolder.getUser().getId(), this);
		super.onDetach(detachEvent);
	}

	@Override
	public void receiveBroadcast(UserNotification notification) {
		UI.getCurrent().access(() -> topBar.updateNotifications(securityHolder.getUserNotifications()));
	}

}
