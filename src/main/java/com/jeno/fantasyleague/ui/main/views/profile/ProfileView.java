package com.jeno.fantasyleague.ui.main.views.profile;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.ui.common.label.StatusLabel;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.ImageUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.StreamResource;

@Tag("profile-view")
@Route(value = State.StateUrlConstants.PROFILE, layout = MainView.class)
public class ProfileView extends VerticalLayout implements RouterLayout {

	private final UserDao userDao;
	private final SecurityHolder securityHolder;

	private VerticalLayout layout;

	@Autowired
	public ProfileView(SecurityHolder securityHolder, UserDao userDao) {
		this.securityHolder = securityHolder;
		this.userDao = userDao;

		initLayout();
		initForm();
	}

	private void initForm() {
		User user = securityHolder.getUser();

		ImageUploadWithPlaceholder uploadLayout = new ImageUploadWithPlaceholder();
		Optional<StreamResource> ogProfilePic = ImageUtil.getUserProfilePictureResource(user);
		ogProfilePic.ifPresent(uploadLayout::updateImage);

		Binder<UserProfileBean> binder = new Binder<>();

		UserProfileBean bean = new UserProfileBean(user);

		StatusLabel infoLabel = new StatusLabel();

		// Create the fields
		TextField username = new TextField("Username");
		username.setValueChangeMode(ValueChangeMode.EAGER);
		username.setWidthFull();
		TextField email = new TextField("E-mail");
		email.setValueChangeMode(ValueChangeMode.EAGER);
		email.setWidthFull();
		CustomButton save = new CustomButton("Save", VaadinIcon.CHECK.create());
		CustomButton reset = new CustomButton("Reset", VaadinIcon.ARROW_BACKWARD.create());

		// Button bar
		HorizontalLayout actions = new HorizontalLayout();
		actions.add(save, reset);

		binder.forField(email)
				.withValidator(new EmailValidator("Not a valid email adress"))
				.bind(UserProfileBean::getEmail, UserProfileBean::setEmail);

		// First name and last name are required fields
		username.setRequiredIndicatorVisible(true);

		binder.forField(username)
				.withValidator(new StringLengthValidator(
						"Please add the username", 1, null))
				.bind(UserProfileBean::getUsername, UserProfileBean::setUsername);
		// Click listeners for the buttons
		save.addClickListener(event -> {
			if (binder.writeBeanIfValid(bean)) {
				String previousUsername = user.getUsername();
				String previousEmail = user.getEmail();
				user.setUsername(bean.getUsername());
				user.setEmail(bean.getEmail());
				uploadLayout.getImage().map(ByteArrayInputStream::readAllBytes).ifPresent(user::setProfile_picture);
				try {
					userDao.update(user);
					infoLabel.setSuccessText("Changes saved");
				} catch (ValidationException e) {
					infoLabel.setErrorText(String.join(",<br/>", e.getErrorMap().values()));
					user.setUsername(previousUsername);
					user.setEmail(previousEmail);
				}
			} else {
				BinderValidationStatus<UserProfileBean> validate = binder.validate();
				String errorText = validate.getFieldValidationStatuses()
						.stream().filter(BindingValidationStatus::isError)
						.map(BindingValidationStatus::getMessage)
						.map(Optional::get).distinct()
						.collect(Collectors.joining(", "));
				infoLabel.setErrorText(errorText);
			}
		});
		reset.addClickListener(event -> {
			binder.readBean(new UserProfileBean(user));
			ogProfilePic.ifPresent(uploadLayout::updateImage);
			infoLabel.reset();
		});
		binder.readBean(bean);

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth(null);
		formLayout.setAlignItems(Alignment.CENTER);
		formLayout.add(uploadLayout, username, email, actions, infoLabel);

		layout.add(formLayout);
	}

	private void initLayout() {
		VerticalLayout rootLayout = new VerticalLayout();
		layout = new VerticalLayout();
		layout.setMaxWidth("1200px");
		layout.setAlignItems(Alignment.CENTER);
		rootLayout.add(layout);
		rootLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(rootLayout);
	}

}
