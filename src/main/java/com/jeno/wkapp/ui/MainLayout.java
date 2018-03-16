package com.jeno.wkapp.ui;

import com.jeno.wkapp.ui.menu.MainMenu;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
public class MainLayout {

    @Autowired
    private MainMenu mainMenu;

    private VerticalLayout mainLayout;

    public void create() {
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        mainMenu.create();
        mainLayout.addComponent(mainMenu.getComponent());
    }

    public Component getComponent() {
        return mainLayout;
    }

}
