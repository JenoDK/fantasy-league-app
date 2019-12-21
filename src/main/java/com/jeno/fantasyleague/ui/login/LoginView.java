package com.jeno.fantasyleague.ui.login;

import com.jeno.fantasyleague.security.SecurityUtils;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route
@PageTitle("Login")
@Viewport(AppConst.VIEWPORT)
public class LoginView extends LoginOverlay
	implements AfterNavigationObserver, BeforeEnterObserver {

	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("Fantasy League");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Sign in");
		i18n.getForm().setTitle("Sign in");
		i18n.getForm().setUsername("Email");
		i18n.getForm().setPassword("Password");
		setI18n(i18n);
		setAction("login");

		setForgotPasswordButtonVisible(true);

	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(MainView.class);
		} else {
			setOpened(true);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		boolean containsError = event.getLocation().getQueryParameters().getParameters().containsKey("error");
		setError(containsError);
	}

}
