package com.jeno.demo.ui;

import com.jeno.demo.data.dao.UserDao;
import com.jeno.demo.data.dao.ValidationException;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.form.UserForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/register")
@Title("Register")
@Theme("valo")
public class RegisterUI extends UI {

	@Autowired
	private UserDao userDao;

	private VerticalLayout mainLayout;
	private Button backToLogin;
	private UserForm form;

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		backToLogin = new Button("Login");
		backToLogin.addClickListener(ignored -> Page.getCurrent().setLocation("/login"));

		form = new UserForm();
		form.validSubmit().subscribe(user -> {
			try {
				User createdUser = userDao.add(user);
				registrationSuccessful(createdUser);
			} catch (ValidationException ex) {
				form.setErrorMap(ex.getErrorMap());
			}
		});

		mainLayout.addComponent(backToLogin);
		mainLayout.addComponent(form);
		mainLayout.setComponentAlignment(backToLogin, Alignment.TOP_LEFT);
		mainLayout.setExpandRatio(backToLogin, 1f);
		mainLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
		mainLayout.setExpandRatio(form, 9f);

		setContent(mainLayout);
	}

	private void registrationSuccessful(User createdUser) {
		mainLayout.removeAllComponents();

		VerticalLayout successLayout = new VerticalLayout();
		successLayout.setHeightUndefined();

		Label successLabel = new Label("Thanks for your registration " + createdUser.getUsername());
		successLabel.addStyleName(ValoTheme.LABEL_LARGE);

		backToLogin.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		successLayout.addComponent(successLabel);
		successLayout.addComponent(backToLogin);
		successLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
		successLayout.setComponentAlignment(backToLogin, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(successLayout);
		mainLayout.setComponentAlignment(successLayout, Alignment.MIDDLE_CENTER);
	}

}
