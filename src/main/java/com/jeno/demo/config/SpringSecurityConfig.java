package com.jeno.demo.config;

import com.jeno.demo.data.security.UserDetailsServiceImpl;
import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableVaadin
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).accessDeniedPage("/accessDenied")
                .and()
            .authorizeRequests()
                .antMatchers(
                        "/VAADIN/**",
                        "/PUSH/**", "/UIDL/**",
                        "/register**",
                        "/register",
                        "/login",
                        "/login/**",
                        "/forgotPassword**",
                        "/forgotPassword",
                        "/error/**",
                        "/accessDenied/**",
                        "/vaadinServlet/**")
                    .permitAll()
                .antMatchers(
                        "/login*",
                        "/register*",
                        "/forgotPassword*")
                    .anonymous()
                .anyRequest()
                    .authenticated()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login")
                    .and()
                .sessionManagement().sessionAuthenticationStrategy(sessionControlAuthenticationStrategy());
    }

    @Bean
    public SessionAuthenticationStrategy sessionControlAuthenticationStrategy(){
        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
        sessionFixationProtectionStrategy.setMigrateSessionAttributes(false);

        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry());

        List<SessionAuthenticationStrategy> strategies = new LinkedList<>();
        strategies.add(sessionFixationProtectionStrategy);
        strategies.add(registerSessionAuthenticationStrategy);

        CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(strategies);

        return compositeSessionAuthenticationStrategy;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public DaoAuthenticationProvider createDaoAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}