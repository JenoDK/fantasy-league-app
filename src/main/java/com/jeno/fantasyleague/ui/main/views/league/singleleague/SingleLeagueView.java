package com.jeno.fantasyleague.ui.main.views.league.singleleague;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.ui.common.LeagueImageResourceCache;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.gridlayout.LeagueBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.chat.ChatBox;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SingleLeagueView extends VerticalLayout {

	private static final Logger LOG = LogManager.getLogger(SingleLeagueView.class.getName());

	private final BehaviorSubject<ClickEvent<?>> backClick = BehaviorSubject.create();
	private final LeagueBean leagueBean;

	private ChatBox chatBox;

	public SingleLeagueView(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.leagueBean = leagueBean;

		initLayout(singleLeagueServiceprovider);
	}

	private void initLayout(SingleLeagueServiceProvider singleLeagueServiceprovider) {
		League league = leagueBean.getLeague();
		setSizeFull();
		setPadding(false);
		setSpacing(false);
		setMaxWidth("1200px");

		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);
		main.setSizeFull();

		LeagueTopBar topBar = new LeagueTopBar(league);
		topBar.imageUploaded().subscribe(os -> {
			try {
				LeagueImageResourceCache.remove(league);
				league.setLeague_picture(os.toByteArray());
				singleLeagueServiceprovider.getLeagueRepository().saveAndFlush(league);
				topBar.updateLeagueImage(league);
			} catch (Exception e) {
				LOG.error("Failed to update league image", e);
			}
		});

		LeagueUser loggedInLeagueUser = singleLeagueServiceprovider.getLoggedInLeagueUser(league);
		if (loggedInLeagueUser.isShow_help()) {
			showHelp(leagueBean, singleLeagueServiceprovider);
			// We only show it the first time, want to avoid spamming.
			loggedInLeagueUser.setShow_help(false);
			singleLeagueServiceprovider.getLeagueUserRepository().saveAndFlush(loggedInLeagueUser);
		}
		chatBox = new ChatBox(league, loggedInLeagueUser, singleLeagueServiceprovider);
		main.add(chatBox);
		LeagueTabs leagueTabs = new LeagueTabs(leagueBean, singleLeagueServiceprovider, main);
		CustomButton helpButton = new CustomButton(VaadinIcon.QUESTION_CIRCLE);
		helpButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE);
		helpButton.addClickListener(event -> showHelp(leagueBean, singleLeagueServiceprovider));
		leagueTabs.add(helpButton);
		add(leagueTabs);
		add(topBar);
		add(main);
	}

	private void showHelp(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		LeagueTipDialog leagueTipDialog = new LeagueTipDialog(leagueBean, singleLeagueServiceprovider);
		leagueTipDialog.show();
	}

	private Component createNavigationRow(String title, Icon icon) {
		CustomButton button = new CustomButton(title, icon);
		button.setSizeFull();
		button.setHeight("100px");
		button.addThemeName("navigation-row");
		return button;
	}

	public Observable<ClickEvent<?>> backToLeaguesView() {
		return backClick;
	}

	public void leagueViewClicked(ClickEvent event) {
		// TODO detect click is outside chatbox
	}
}
