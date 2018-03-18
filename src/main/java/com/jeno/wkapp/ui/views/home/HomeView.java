package com.jeno.wkapp.ui.views.home;

import com.jeno.wkapp.data.security.CustomUserDetails;
import com.jeno.wkapp.model.RoleName;
import com.jeno.wkapp.ui.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = State.StateUrlConstants.HOME)
public class HomeView extends VerticalLayout implements View {

    @PostConstruct
    void init() {
        addComponent(new Label(getGreeting()));
    }

    private String getGreeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> RoleName.ROLE_ADMIN.toString().equals(auth.getAuthority()));
        return "Greetings " + (isAdmin ? "admin" : "user") + " " + userDetails.getUsername();
    }

}
