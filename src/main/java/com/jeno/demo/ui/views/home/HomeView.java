package com.jeno.demo.ui.views.home;

import com.jeno.demo.ui.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = State.StateUrlConstants.HOME)
public class HomeView extends VerticalLayout implements View {

    @PostConstruct
    void init() {
        addComponent(new Label("I am a home view"));
    }

}
