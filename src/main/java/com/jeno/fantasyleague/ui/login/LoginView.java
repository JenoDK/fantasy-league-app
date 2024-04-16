package com.jeno.fantasyleague.ui.login;

import com.jeno.fantasyleague.security.SecurityUtils;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.ui.forgotpassword.ForgotPasswordUI;
import com.jeno.fantasyleague.ui.main.views.league.LeagueModule;
import com.jeno.fantasyleague.ui.register.RegisterUI;
import com.jeno.fantasyleague.util.AppConst;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
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
		i18n.getForm().setUsername("Username or email");
		i18n.getForm().setPassword("Password");
		loginForm = new LoginForm(i18n);
		loginForm.setForgotPasswordButtonVisible(false);
		loginForm.setAction("login");
		add(loginForm);
		Anchor googleLoginLink = new Anchor("/oauth2/authorization/google");
		// Generated with https://developers.google.com/identity/branding-guidelines, add the css to login-styles.css
		googleLoginLink.getElement().setProperty("innerHTML",
				"<button class=\"gsi-material-button\">\n" +
				"  <div class=\"gsi-material-button-state\"></div>\n" +
				"  <div class=\"gsi-material-button-content-wrapper\">\n" +
				"    <div class=\"gsi-material-button-icon\">\n" +
				"      <svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 48 48\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" style=\"display: block;\">\n" +
				"        <path fill=\"#EA4335\" d=\"M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z\"></path>\n" +
				"        <path fill=\"#4285F4\" d=\"M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z\"></path>\n" +
				"        <path fill=\"#FBBC05\" d=\"M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z\"></path>\n" +
				"        <path fill=\"#34A853\" d=\"M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z\"></path>\n" +
				"        <path fill=\"none\" d=\"M0 0h48v48H0z\"></path>\n" +
				"      </svg>\n" +
				"    </div>\n" +
				"    <span class=\"gsi-material-button-contents\">Continue with Google</span>\n" +
				"    <span style=\"display: none;\">Continue with Google</span>\n" +
				"  </div>\n" +
				"</button>");
		add(googleLoginLink);

		Anchor facebookLoginLink = new Anchor("/oauth2/authorization/facebook");
		// https://codepen.io/davidelrizzo/pen/vEYvyv
		facebookLoginLink.getElement().setProperty("innerHTML",
				"<button class=\"loginBtn loginBtn--facebook\">\n" +
				"  Continue with Facebook\n" +
				"</button>");
		add(facebookLoginLink);

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
