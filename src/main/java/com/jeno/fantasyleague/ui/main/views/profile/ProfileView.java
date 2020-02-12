package com.jeno.fantasyleague.ui.main.views.profile;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.ui.main.MainView;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Tag("profile-view")
@Route(value = State.StateUrlConstants.PROFILE, layout = MainView.class)
public class ProfileView extends VerticalLayout {

    @Autowired
    public ProfileView(SecurityHolder securityHolder, UserDao userDao) {
        User user = securityHolder.getUser();

        ImageUploadWithPlaceholder uploadLayout = new ImageUploadWithPlaceholder(user);
        uploadLayout.imageUploadedAndResized().subscribe(file -> {
            user.setProfile_picture(file.readAllBytes());
            userDao.update(user);
        });

        UserProfileBean bean = new UserProfileBean(user.getUsername());
        Binder<UserProfileBean> binder = new Binder<>();
        TextField userNameField = new TextField("Username");
        binder.forField(userNameField)
                .bind(UserProfileBean::getUsername, UserProfileBean::setUsername);
        binder.setBean(bean);
        binder.addStatusChangeListener(status -> {
            if (!status.hasValidationErrors()) {
                String previousUsername = user.getUsername();
                user.setUsername(bean.getUsername());
                try {
                    userDao.update(user);
                } catch (ValidationException e) {
                    userNameField.setErrorMessage(String.join(",<br/>", e.getErrorMap().values()));
                    user.setUsername(previousUsername);
                }
            }
        });
        add(uploadLayout);
        add(userNameField);
    }

}
