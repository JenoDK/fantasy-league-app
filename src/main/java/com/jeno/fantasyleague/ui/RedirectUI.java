package com.jeno.fantasyleague.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class RedirectUI extends Div {

	private final String redirectButtonText;
	private final String redirectPath;

	protected VerticalLayout mainLayout;
	protected Button redirectButton;

	public RedirectUI(String redirectButtonText, String redirectPath) {
		this.redirectButtonText = redirectButtonText;
		this.redirectPath = redirectPath;

		initLayout();
	}

	protected void initLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		redirectButton = new Button(redirectButtonText, VaadinIcon.ARROW_CIRCLE_LEFT.create());
		redirectButton.addClickListener(ignored -> UI.getCurrent().navigate(redirectPath));

		Component middleComponent = getMiddleComponent();

		mainLayout.add(redirectButton);
		mainLayout.add(middleComponent);
//		mainLayout.setComponentAlignment(redirectButton, Alignment.TOP_LEFT);
		mainLayout.setFlexGrow(1, redirectButton);
//		mainLayout.setComponentAlignment(middleComponent, Alignment.MIDDLE_CENTER);
		mainLayout.setFlexGrow(9, middleComponent);

		add(mainLayout);
	}

	protected abstract Component getMiddleComponent();

	protected void actionSuccessful(String successMessage) {
		mainLayout.removeAll();

		VerticalLayout successLayout = new VerticalLayout();
		successLayout.setHeight(null);

		Label successLabel = new Label(successMessage);
//		successLabel.addClassName(ValoTheme.LABEL_LARGE);
//		successLabel.addClassName(ValoTheme.LABEL_SUCCESS);

		successLayout.add(successLabel);
		successLayout.add(redirectButton);
//		successLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
//		successLayout.setComponentAlignment(redirectButton, Alignment.MIDDLE_CENTER);

		mainLayout.add(successLayout);
//		mainLayout.setComponentAlignment(successLayout, Alignment.MIDDLE_CENTER);
	}

}
