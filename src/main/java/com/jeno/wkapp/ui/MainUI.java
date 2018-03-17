package com.jeno.wkapp.ui;

import com.jeno.wkapp.ui.navigation.NavigationBar;
import com.jeno.wkapp.ui.views.accessdenied.AccessDeniedView;
import com.jeno.wkapp.ui.views.error.ErrorView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class MainUI extends UI {

    private final SpringViewProvider viewProvider;

    @Autowired
    public MainUI(SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        root.addComponent(new NavigationBar());

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
