package com.jeno.fantasyleague.ui.login;

import com.jeno.fantasyleague.security.SecurityUtils;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.ui.forgotpassword.ForgotPasswordUI;
import com.jeno.fantasyleague.ui.main.views.league.LeagueModule;
import com.jeno.fantasyleague.ui.register.RegisterUI;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route
@PageTitle("Login")
@AlwaysAllow
@CssImport("./styles/login-styles.css")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Viewport(AppConst.VIEWPORT)
public class LoginView extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver {

	private LoginForm loginForm;

	public LoginView() {
		initLayout();
	}

	private void initLayout() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("My Starter Project");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Sign in");
		i18n.getForm().setTitle("Sign in");
		i18n.getForm().setUsername("Username");
		i18n.getForm().setPassword("Password");
		loginForm = new LoginForm(i18n);
		loginForm.setForgotPasswordButtonVisible(false);
		loginForm.setAction("login");
		add(loginForm);

		RouterLink registerLink = new RouterLink("Register", RegisterUI.class);
		add(registerLink);
		RouterLink forgotPwLink = new RouterLink("Forgot password", ForgotPasswordUI.class);
		add(forgotPwLink);

		setAlignItems(Alignment.CENTER);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(LeagueModule.class);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		loginForm.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}

}
