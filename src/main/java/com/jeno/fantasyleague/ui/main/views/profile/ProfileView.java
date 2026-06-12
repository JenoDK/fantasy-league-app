package com.jeno.fantasyleague.ui.main.views.profile;

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
import com.jeno.fantasyleague.util.Images;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag("profile-view")
@Route(value = State.StateUrlConstants.PROFILE, layout = MainView.class)
@PageTitle("Profile")
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
		String ogProfilePicSrc = user.hasProfilePicture() ? ImageUtil.getProfileImageUrl(user) : Images.DEFAULT_PROFILE_PICTURE;
		uploadLayout.updateImage(ogProfilePicSrc);

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
		Checkbox reminderEmails = new Checkbox("Receive reminder emails");
		reminderEmails.setWidthFull();
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

		binder.forField(reminderEmails)
				.bind(UserProfileBean::isReminderEmailsEnabled, UserProfileBean::setReminderEmailsEnabled);
		// Click listeners for the buttons
		save.addClickListener(event -> {
			if (binder.writeBeanIfValid(bean)) {
				String previousUsername = user.getUsername();
				String previousEmail = user.getEmail();
				user.setUsername(bean.getUsername());
				user.setEmail(bean.getEmail());
				user.setReminder_emails_enabled(bean.isReminderEmailsEnabled());
				Optional<byte[]> newProfilePicture = uploadLayout.getImage().map(ByteArrayInputStream::readAllBytes);
				try {
					userDao.update(user);
					newProfilePicture.ifPresent(picture -> userDao.updateProfilePicture(user, picture));
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
			uploadLayout.updateImage(ogProfilePicSrc);
			infoLabel.reset();
		});
		binder.readBean(bean);

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth(null);
		formLayout.setAlignItems(Alignment.CENTER);
		formLayout.add(uploadLayout, username, email, reminderEmails, actions, infoLabel);

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
