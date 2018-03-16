package com.jeno.wkapp.ui.menu;

import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
public class MainMenu {

    private HorizontalLayout menuBar;

    public void create() {
        menuBar = new HorizontalLayout();
        menuBar.setWidth("100%");
        menuBar.setHeightUndefined();

        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(this::logout);

        menuBar.addComponent(logoutButton);
        menuBar.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
    }

    public Component getComponent() {
        return menuBar;
    }

    private void logout(Button.ClickEvent event) {
        String logoutUrl = VaadinService.getCurrentRequest().getContextPath() + "/logout";
        UI.getCurrent().getPage().setLocation(logoutUrl);
    }

}
