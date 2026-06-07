package com.jeno.fantasyleague.security;

import com.jeno.fantasyleague.backend.model.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the {@link UserDetailsServiceImpl}.</li>

 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";

	// Keep users logged in for ~395 days (the practical maximum a browser will retain a cookie).
	private static final int REMEMBER_ME_VALIDITY_SECONDS = 395 * 24 * 60 * 60;

	private final UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Fixed secret so remember-me cookies stay valid across app restarts/redeploys. Override per
	// environment via the app.remember-me.key property.
	@Value("${app.remember-me.key:bf3c1a9e-7d24-4e8b-9a1f-fantasy-league-2026}")
	private String rememberMeKey;
	
	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	/**
	 * Registers our UserDetailsService and the password encoder to be used on login attempts.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()

				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache().requestCache(new CustomRequestCache())

				// Restrict access to our application.
				.and().authorizeRequests()

				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
				.antMatchers("/actuator/**").hasRole("ENDPOINT_ADMIN")

				// Allow all requests by logged in users.
				.anyRequest().hasAnyAuthority(RoleName.allRoles())
				.and().httpBasic()

				// Configure the login page.
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)

				// Register the success handler that redirects users to the page they last tried
				// to access
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

				// Configure logout
				.and().logout().logoutSuccessUrl("/login")

				// Keep users logged in via a long-lived remember-me cookie. alwaysRemember is used
				// because the plain HTML login form has no "remember me" checkbox: every successful
				// login should be remembered. This re-authenticates the user on a new request even
				// after the server-side session has ended (timeout, restart, ...).
				.and().rememberMe()
				.rememberMeServices(rememberMeServices())

				// Configure OAuth2
				.and().oauth2Login().loginPage(LOGIN_URL).successHandler(authenticationSuccessHandler);
	}

	/**
	 * Remember-me services for form/password logins.
	 *
	 * <p>The remember-me filter runs for every authentication mechanism, including OAuth2. The
	 * default {@link TokenBasedRememberMeServices} cannot build a token for an OAuth2/OIDC principal
	 * (it has no password) and falls back to looking the user up via {@link UserDetailsService} using
	 * {@code principal.toString()}, which throws {@code UsernameNotFoundException} and aborts the
	 * OAuth2 login. We therefore only issue a remember-me cookie when the principal is a
	 * {@link UserDetails} (i.e. a form/password login).
	 */
	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices services =
				new TokenBasedRememberMeServices(rememberMeKey, userDetailsService) {
					@Override
					public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication successfulAuthentication) {
						if (successfulAuthentication.getPrincipal() instanceof UserDetails) {
							super.onLoginSuccess(request, response, successfulAuthentication);
						}
					}
				};
		services.setAlwaysRemember(true);
		services.setTokenValiditySeconds(REMEMBER_ME_VALIDITY_SECONDS);
		// Unique cookie name so it cannot collide with other apps on the same domain.
		services.setCookieName("FANTASYLEAGUEREMEMBERME");
		return services;
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest",
				"/sw.js",
				"/offline-page.html",

				// icons and images
				"/icons/**",
				"/images/**",

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**",

				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**",

				// Application specific resources
				"/register",
				"/register**",
				"/forgotPassword**",
				"/forgotPassword",
				"/resetPassword**",
				"/resetPassword",
				"/activateAccount**",
				"/activateAccount",
				"/error/**",
				"/accessDenied/**");
	}
}
