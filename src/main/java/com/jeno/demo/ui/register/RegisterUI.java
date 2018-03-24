package com.jeno.demo.ui.register;

import com.jeno.demo.data.dao.UserDao;
import com.jeno.demo.data.dao.ValidationException;
import com.jeno.demo.data.service.email.AccountActivationService;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.RedirectUI;
import com.jeno.demo.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.demo.util.VaadinUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import io.reactivex.functions.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;

@SpringUI(path = "/register")
@Title("Register")
@Theme("valo")
public class RegisterUI extends RedirectUI {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountActivationService accountActivationService;

	@Value("${account.activation.required}")
	private boolean accountActivationRequired;

	private RegisterUserForm form;
	private ImageUploadWithPlaceholder profilePictureUploader;
	private HorizontalLayout middleComponent;

	public RegisterUI() {
		super("Login", "/login");
	}

	@Override
	protected Component getMiddleComponent() {
		middleComponent = new HorizontalLayout();
		middleComponent.setSizeFull();

		form = new RegisterUserForm();
		form.validSubmit().subscribe(this::addUser);

		profilePictureUploader = new ImageUploadWithPlaceholder();

		middleComponent.addComponent(form);
		middleComponent.addComponent(profilePictureUploader);
		middleComponent.setComponentAlignment(form, Alignment.TOP_RIGHT);
		middleComponent.setComponentAlignment(profilePictureUploader, Alignment.TOP_LEFT);

		return middleComponent;
	}

	private void addUser(User user) throws IOException {
		try {
			if (profilePictureUploader.getImage().isPresent()) {
				user.setProfile_picture(Files.readAllBytes(profilePictureUploader.getImage().get().toPath()));
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
