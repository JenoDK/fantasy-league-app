package com.jeno.fantasyleague.ui.login;

import com.jeno.fantasyleague.ui.RedirectUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.LoginForm;
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
@Theme("fantasy-league")
public class LoginUI extends RedirectUI {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    private CustomLoginForm loginForm;

    public LoginUI() {
        super("Register", "/register");
    }

    @Override
    public void doInit(VaadinRequest request, int uiId, String embedId) {
        // If user authenticated, redirect to main page
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            Page.getCurrent().setLocation("/");
            return;
        }

        super.doInit(request, uiId, embedId);
    }

    @Override
    protected Component getMiddleComponent() {
        loginForm = new CustomLoginForm();
        loginForm.addLoginListener(this::loginEvent);
        return loginForm;
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
            ex.printStackTrace();
            loginForm.setError(ex.getMessage());
        }
    }

}
