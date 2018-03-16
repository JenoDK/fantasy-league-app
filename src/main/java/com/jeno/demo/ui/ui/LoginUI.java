package com.jeno.demo.ui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
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
@Title("LoginPage")
@Theme("valo")
public class LoginUI extends UI {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        // If user authenticated, redirect to main page
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            Page.getCurrent().setLocation("/");
            return;
        }

        Label title = new Label("De mega super deluxe WK pronostiek");
        title.setStyleName("login_page_title");
        title.setContentMode(ContentMode.HTML);

        VerticalLayout layout = new VerticalLayout();
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::loginEvent);

        layout.addComponent(loginForm);
        layout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        layout.setSizeFull();
        setContent(layout);
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
            String message = "Incorrect user or password";
            Notification.show(message, Notification.Type.ERROR_MESSAGE);
        }
    }

}
