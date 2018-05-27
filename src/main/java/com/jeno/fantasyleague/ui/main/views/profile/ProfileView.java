package com.jeno.fantasyleague.ui.main.views.profile;

import java.nio.file.Files;

import javax.annotation.PostConstruct;

import com.jeno.fantasyleague.data.repository.UserRepository;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.ui.common.image.ImageUploadWithPlaceholder;
import com.jeno.fantasyleague.ui.main.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = State.StateUrlConstants.PROFILE)
public class ProfileView extends VerticalLayout implements View {

    @Autowired
    private SecurityHolder securityHolder;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    void init() {
        addComponent(new Label("Hi " + securityHolder.getUser().getUsername() + ", I could have been a profile view, but my developer was to lazy.<br/><br/>" +
                "Ok maybe I'll just let you adjust your profile picture...", ContentMode.HTML));

        User currentUser = securityHolder.getUser();
        ImageUploadWithPlaceholder uploadLayout = new ImageUploadWithPlaceholder(currentUser);
        uploadLayout.imageUploadedAndResized().subscribe(file -> {
            currentUser.setProfile_picture(Files.readAllBytes(file.toPath()));
            userRepository.saveAndFlush(currentUser);
        });
        addComponent(uploadLayout);
    }

}
