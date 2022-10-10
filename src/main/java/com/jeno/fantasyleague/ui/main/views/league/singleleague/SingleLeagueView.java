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
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SingleLeagueView extends VerticalLayout {

	private static final Logger LOG = LogManager.getLogger(SingleLeagueView.class.getName());

	private final BehaviorSubject<ClickEvent<Button>> backClick = BehaviorSubject.create();
	private final LeagueBean leagueBean;

	private LeagueMenuBar tabSheet;

	public SingleLeagueView(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.leagueBean = leagueBean;

		initLayout(leagueBean, singleLeagueServiceprovider);
	}

	private void initLayout(LeagueBean leagueBean, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		League league = leagueBean.getLeague();
		setSizeFull();
		setMaxWidth("1200px");

		Button back = new Button(VaadinIcon.ARROW_LEFT.create());
		back.addClickListener(backClick::onNext);

		Button help = new Button(VaadinIcon.QUESTION_CIRCLE.create());
		help.addClickListener(ignored -> showHelp(leagueBean, singleLeagueServiceprovider));

		HorizontalLayout navigation = new HorizontalLayout();
		navigation.setWidthFull();

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


		tabSheet = new LeagueMenuBar(league, singleLeagueServiceprovider, main);
		navigation.add(tabSheet);

		HorizontalLayout topButtonBar = new HorizontalLayout();
		topButtonBar.setSpacing(false);
		topButtonBar.setPadding(false);
		topButtonBar.setMargin(false);
		topButtonBar.setHeight(null);
		topButtonBar.setWidthFull();
		VaadinUtil.addStyles(topButtonBar.getStyle(),
				"padding: var(--lumo-space-xs);\n");
		topButtonBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		topButtonBar.setAlignItems(FlexComponent.Alignment.END);
		topButtonBar.add(back, help);

		add(topButtonBar);
		LeagueUser loggedInLeagueUser = singleLeagueServiceprovider.getLoggedInLeagueUser(league);
		if (loggedInLeagueUser.isShow_help()) {
			showHelp(leagueBean, singleLeagueServiceprovider);
			// We only show it the first time, want to avoid spamming.
			loggedInLeagueUser.setShow_help(false);
			singleLeagueServiceprovider.getLeagueUserRepository().saveAndFlush(loggedInLeagueUser);
		}
		main.add(new ChatBox(league, loggedInLeagueUser, singleLeagueServiceprovider));
		add(topBar);
		add(navigation);
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

	public Observable<ClickEvent<Button>> backToLeaguesView() {
		return backClick;
	}

}
