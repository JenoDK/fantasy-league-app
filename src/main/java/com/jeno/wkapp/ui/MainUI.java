package com.jeno.wkapp.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class MainUI extends UI {

    @Autowired
    private MainLayout mainLayout;

    @Override
    protected void init(VaadinRequest request) {
        mainLayout.create();
        setContent(mainLayout.getComponent());
    }

}
