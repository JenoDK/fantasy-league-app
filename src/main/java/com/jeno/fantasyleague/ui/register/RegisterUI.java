package com.jeno.fantasyleague.ui.register;

import java.io.IOException;
import java.nio.file.Files;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.data.service.email.AccountActivationService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.util.AppConst;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@PageTitle("Register")
@Route(value = "register")
@AlwaysAllow
@Viewport(AppConst.VIEWPORT)
public class RegisterUI extends RedirectUI {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountActivationService accountActivationService;

	@Value("${account.activation.required}")
	private boolean accountActivationRequired;

	private RegisterUserForm form;

	public RegisterUI() {
		super();
	}

	@Override
	protected Component getMiddleComponent() {
		form = new RegisterUserForm();
		form.validSubmit().subscribe(this::addUser);
		return form;
	}

	private void addUser(User user) throws IOException {
		try {
			if (form.getProfilePictureUploader().getImage().isPresent()) {
				user.setProfile_picture(Files.readAllBytes(form.getProfilePictureUploader().getImage().get().toPath()));
			}
			if (accountActivationRequired) {
				User createdUser = userDao.add(user);
				accountActivationService.sendAccountActivationEmail(createdUser, VaadinUtil.getRootRequestURL());
				StringBuilder sb = new StringBuilder();
				sb.append("Thanks for your registration " + createdUser.getUsername());
				sb.append("<br/>An e-mail has been sent to " + createdUser.getEmail() + " to activate your account");
				actionSuccessful(sb.toString());
			} else {
				user.setActive(true);
				User createdUser = userDao.add(user);
				actionSuccessful("Thanks for your registration " + createdUser.getUsername());
			}
		} catch (ValidationException ex) {
			form.setErrorMap(ex.getErrorMap());
		}
	}

}
