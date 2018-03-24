package com.jeno.demo.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public abstract class RedirectUI extends UI {

	private final String redirectButtonText;
	private final String redirectPath;

	protected VerticalLayout mainLayout;
	protected Button redirectButton;

	public RedirectUI(String redirectButtonText, String redirectPath) {
		this.redirectButtonText = redirectButtonText;
		this.redirectPath = redirectPath;
	}

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		redirectButton = new Button(redirectButtonText, VaadinIcons.ARROW_CIRCLE_LEFT);
		redirectButton.addClickListener(ignored -> Page.getCurrent().setLocation(redirectPath));

		Component middleComponent = getMiddleComponent();

		mainLayout.addComponent(redirectButton);
		mainLayout.addComponent(middleComponent);
		mainLayout.setComponentAlignment(redirectButton, Alignment.TOP_LEFT);
		mainLayout.setExpandRatio(redirectButton, 1f);
		mainLayout.setComponentAlignment(middleComponent, Alignment.MIDDLE_CENTER);
		mainLayout.setExpandRatio(middleComponent, 9f);

		setContent(mainLayout);
	}

	protected abstract Component getMiddleComponent();

	protected void actionSuccessful(String successMessage) {
		mainLayout.removeAllComponents();

		VerticalLayout successLayout = new VerticalLayout();
		successLayout.setHeightUndefined();

		Label successLabel = new Label(successMessage, ContentMode.HTML);
		successLabel.addStyleName(ValoTheme.LABEL_LARGE);
		successLabel.addStyleName(ValoTheme.LABEL_SUCCESS);

		successLayout.addComponent(successLabel);
		successLayout.addComponent(redirectButton);
		successLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
		successLayout.setComponentAlignment(redirectButton, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(successLayout);
		mainLayout.setComponentAlignment(successLayout, Alignment.MIDDLE_CENTER);
	}

}
