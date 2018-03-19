package com.jeno.demo.ui.register;

import com.jeno.demo.data.dao.UserDao;
import com.jeno.demo.data.dao.ValidationException;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.RedirectUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/register")
@Title("Register")
@Theme("valo")
public class RegisterUI extends RedirectUI {

	@Autowired
	private UserDao userDao;

	private RegisterUserForm form;

	public RegisterUI() {
		super("Login", "/login");
	}

	@Override
	protected Component getMiddleComponent() {
		form = new RegisterUserForm();
		form.validSubmit().subscribe(user -> {
			try {
				User createdUser = userDao.add(user);
				actionSuccessful("Thanks for your registration " + createdUser.getUsername());
			} catch (ValidationException ex) {
				form.setErrorMap(ex.getErrorMap());
			}
		});
		return form;
	}

}
