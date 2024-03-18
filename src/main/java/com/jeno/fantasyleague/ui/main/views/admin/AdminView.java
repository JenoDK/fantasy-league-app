package com.jeno.fantasyleague.ui.main.views.admin;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class AdminView {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;

	private boolean init = false;

	private VerticalLayout rootLayout;
	private Tabs tabs;

	private AdminTabs adminTabs;

	@Autowired
	public AdminView(SingleLeagueServiceProvider singleLeagueServiceProvider) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
	}

	private void initLayout(Optional<League> optionalLeague) {
		rootLayout = new VerticalLayout();
		rootLayout.setPadding(false);
		rootLayout.setSpacing(false);
		rootLayout.setMaxWidth("1200px");
		rootLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		VerticalLayout main = new VerticalLayout();
		main.setPadding(false);

		adminTabs = new AdminTabs(optionalLeague, singleLeagueServiceProvider, main);

		rootLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		rootLayout.add(adminTabs, main);

		init = true;
	}

	public VerticalLayout getLayout(Optional<League> optionalLeague) {
		if (!init) {
			initLayout(optionalLeague);
		}
		return rootLayout;
	}

}
