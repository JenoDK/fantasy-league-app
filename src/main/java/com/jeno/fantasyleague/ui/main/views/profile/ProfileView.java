package com.jeno.fantasyleague.ui.main.views.profile;

import java.nio.file.Files;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.data.dao.UserDao;
import com.jeno.fantasyleague.data.dao.ValidationException;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = State.StateUrlConstants.PROFILE)
public class ProfileView extends VerticalLayout implements View {

    @Autowired
    private SecurityHolder securityHolder;
    @Autowired
    private UserDao userDao;

    @PostConstruct
    void init() {
        User currentUser = securityHolder.getUser();

        ImageUploadWithPlaceholder uploadLayout = new ImageUploadWithPlaceholder(currentUser);
        uploadLayout.imageUploadedAndResized().subscribe(file -> {
            currentUser.setProfile_picture(Files.readAllBytes(file.toPath()));
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
                    userNameField.setComponentError(
                            VaadinUtil.getComponentError(e.getErrorMap().values().stream().collect(Collectors.joining(",<br/>"))));
                    currentUser.setUsername(previousUsername);
                }
            }
        });
        addComponent(uploadLayout);
        addComponent(userNameField);
    }

}
