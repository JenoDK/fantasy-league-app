package com.jeno.demo.ui.main.views.profile;

import com.jeno.demo.ui.main.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = State.StateUrlConstants.PROFILE)
public class ProfileView extends VerticalLayout implements View {

    @PostConstruct
    void init() {
        addComponent(new Label("I could have been a profile view, but my developer was to lazy"));
    }

}
