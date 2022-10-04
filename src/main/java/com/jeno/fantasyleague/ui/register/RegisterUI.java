package com.jeno.fantasyleague.ui.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.dao.ValidationException;
import com.jeno.fantasyleague.backend.data.repository.EmailLeagueInviteRepository;
import com.jeno.fantasyleague.backend.data.service.email.AccountActivationService;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.RedirectUI;
import com.jeno.fantasyleague.ui.annotation.AlwaysAllow;
import com.jeno.fantasyleague.util.AppConst;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@PageTitle("Register")
@Route(value = "register")
@AlwaysAllow
@Viewport(AppConst.VIEWPORT)
public class RegisterUI extends RedirectUI implements RouterLayout {

	@Autowired
	private UserDao userDao;
	@Autowired
	private EmailLeagueInviteRepository emailLeagueInviteRepository;
	@Autowired
	private AccountActivationService accountActivationService;
	@Autowired
	private LeagueService leagueService;

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

	private void addUser(User user) {
		try {
			if (form.getProfilePictureUploader().getImage().isPresent()) {
				user.setProfile_picture(form.getProfilePictureUploader().getImage().get().readAllBytes());
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
				emailLeagueInviteRepository.findByEmail(user.getEmail()).forEach(emailLeagueInvite -> {
					leagueService.addUserToLeague(emailLeagueInvite.getLeague(), createdUser);
					emailLeagueInvite.setConsumed(true);
					emailLeagueInviteRepository.save(emailLeagueInvite);
				});
				actionSuccessful("Thanks for your registration " + createdUser.getUsername());
			}
		} catch (ValidationException ex) {
			form.setErrorMap(ex.getErrorMap());
		}
	}

}
