package com.jeno.fantasyleague.ui.main;

import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.ui.main.views.accessdenied.AccessDeniedView;
import com.jeno.fantasyleague.ui.main.views.error.ErrorView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("fantasy-league")
public class MainUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private SecurityHolder securityHolder;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        root.addComponent(new NavigationBar(securityHolder.getUser()));

        // View container, navigation results go in here
        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        // If access is refused, show this (bean class of type View needs to be passed in)
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);

        Navigator navigator = new Navigator(this, viewContainer);
        // View for when no view matching navigation is found
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
    }

}
