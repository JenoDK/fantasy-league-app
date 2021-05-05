package com.jeno.fantasyleague.ui;

import com.jeno.fantasyleague.ui.login.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public abstract class RedirectUI extends VerticalLayout {

	protected VerticalLayout mainLayout;
	protected RouterLink redirectLink;

	public RedirectUI() {
		initLayout();
	}

	protected void initLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		redirectLink = new RouterLink("Back to login", LoginView.class);

		Component middleComponent = getMiddleComponent();

		mainLayout.add(middleComponent);
		mainLayout.add(redirectLink);
		mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, redirectLink);
		mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, middleComponent);

		add(mainLayout);
	}

	protected abstract Component getMiddleComponent();

	protected void actionSuccessful(String successMessage) {
		mainLayout.removeAll();

		H2 successTitle = new H2(successMessage);

		mainLayout.add(redirectLink);
		mainLayout.add(successTitle);
		mainLayout.setHorizontalComponentAlignment(Alignment.START, redirectLink);
		mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, successTitle);
	}

}
