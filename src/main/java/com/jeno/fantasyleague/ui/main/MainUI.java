package com.jeno.fantasyleague.ui.main;

import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.ui.main.broadcast.Broadcaster;
import com.jeno.fantasyleague.ui.main.navigation.TopBar;
import com.jeno.fantasyleague.ui.main.views.accessdenied.AccessDeniedView;
import com.jeno.fantasyleague.ui.main.views.error.ErrorView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("fantasy-league")
@Push(value = PushMode.AUTOMATIC)
public class MainUI extends UI implements Broadcaster.BroadcastListener {

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private SecurityHolder securityHolder;
    @Autowired
    private NotificationModel notificationModel;

    private TopBar topBar;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.addStyleName("main-layout");
        root.setSpacing(false);
        root.setMargin(false);
        root.setSizeFull();
        setContent(root);

        topBar = new TopBar(securityHolder.getUser(), securityHolder.getUserNotifications(), notificationModel);
        root.addComponent(topBar);

        // View container, navigation results go in here
        final Panel viewContainer = new Panel();
        viewContainer.addStyleName("view-container");
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        // If access is refused, show this (bean class of type View needs to be passed in)
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);

        Navigator navigator = new Navigator(this, viewContainer);
        // View for when no view matching navigation is found
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);

        // Register to receive broadcasts
        Broadcaster.register(securityHolder.getUser().getId(), this);
    }

    // Must also unregister when the UI expires
    @Override
    public void detach() {
        Broadcaster.unregister(securityHolder.getUser().getId(), this);
        super.detach();
    }

    @Override
    public void receiveBroadcast(UserNotification notification) {
        access(() -> topBar.updateNotifications(securityHolder.getUserNotifications()));
    }

}
