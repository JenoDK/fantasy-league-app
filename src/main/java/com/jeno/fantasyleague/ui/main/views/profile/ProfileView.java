package com.jeno.fantasyleague.ui.main.views.profile;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = State.StateUrlConstants.PROFILE)
public class ProfileView extends VerticalLayout {

    @Autowired
    private SecurityHolder securityHolder;
    @Autowired
    private UserDao userDao;

    @PostConstruct
    void init() {
        User currentUser = securityHolder.getUser();

        ImageUploadWithPlaceholder uploadLayout = new ImageUploadWithPlaceholder(currentUser);
        uploadLayout.imageUploadedAndResized().subscribe(file -> {
            currentUser.setProfile_picture(file.readAllBytes());
            userDao.update(currentUser);
        });

        UserProfileBean bean = new UserProfileBean(currentUser.getUsername());
        Binder<UserProfileBean> binder = new Binder<>();
        TextField userNameField = new TextField("Username");
        binder.forField(userNameField)
                .bind(UserProfileBean::getUsername, UserProfileBean::setUsername);
        binder.setBean(bean);
        binder.addStatusChangeListener(status -> {
            if (!status.hasValidationErrors()) {
                String previousUsername = currentUser.getUsername();
                currentUser.setUsername(bean.getUsername());
                try {
                    userDao.update(currentUser);
                } catch (ValidationException e) {
                    userNameField.setErrorMessage(String.join(",<br/>", e.getErrorMap().values()));
                    currentUser.setUsername(previousUsername);
                }
            }
        });
        add(uploadLayout);
        add(userNameField);
    }

}
