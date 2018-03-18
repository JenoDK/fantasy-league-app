package com.jeno.wkapp.ui;

import com.jeno.wkapp.ui.form.CustomLoginForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@SpringUI(path = "/login")
@Title("Login")
@Theme("valo")
public class LoginUI extends UI {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    private VerticalLayout mainLayout;
    private Button registerButton;
    private CustomLoginForm loginForm;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        // If user authenticated, redirect to main page
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            Page.getCurrent().setLocation("/");
            return;
        }
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        registerButton = new Button("Register");
        registerButton.addClickListener(ignored -> Page.getCurrent().setLocation("/register"));

        loginForm = new CustomLoginForm();
        loginForm.addLoginListener(this::loginEvent);

        mainLayout.addComponent(registerButton);
        mainLayout.addComponent(loginForm);
        mainLayout.setComponentAlignment(registerButton, Alignment.TOP_LEFT);
        mainLayout.setExpandRatio(registerButton, 1f);
        mainLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(loginForm, 9f);

        setContent(mainLayout);
    }

    private void loginEvent(LoginForm.LoginEvent e) {
        final Authentication auth = new UsernamePasswordAuthenticationToken(e.getLoginParameter("username"), e.getLoginParameter("password"));
        try {
            // this is the code for achieving the spring security authentication in a programmatic way
            final Authentication authenticated = authenticationProvider.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            sessionAuthenticationStrategy.onAuthentication(auth, ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest(), ((VaadinServletResponse)VaadinService.getCurrentResponse()).getHttpServletResponse());
            // Go to HomeUI
            Page.getCurrent().setLocation("/");
        } catch (final AuthenticationException ex) {
            loginForm.setError("Incorrect user or password");
        }
    }

}
