package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.enums.Template;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.util.Images;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import io.reactivex.Observable;

import java.util.Map;

@JsModule("./styles/league-form-styles.js")
public class LeagueForm extends HorizontalLayout {

	private TextField nameField;
	private TextArea description;

	private CustomButton submit;

	private BeanValidationBinder<League> binder = new BeanValidationBinder<>(League.class);

	public LeagueForm() {
		super();
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(nameField).bind("name");
		binder.forField(description).bind("description");
		reset();
	}

	private void initLayout() {
		setSizeUndefined();
		setSpacing(false);
		setMargin(false);
		getStyle().set("--lumo-border-radius", "0.5em");

		Image icon = new Image(Images.Icons.FIFA_WORLD_CUP_2022, "FIFA WORLD CUP QATAR 2022");
		icon.addClassName("league-logo");
		icon.setWidth("150px");
		add(icon);

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(false);
		formLayout.setMargin(false);

		nameField = new TextField("Name");
		nameField.setPlaceholder("Awesome league");

		description = new TextArea("Description");
		description.setPlaceholder("Tom won't win this one!");

		formLayout.add(nameField);
		formLayout.add(description);

		submit = new CustomButton(getTranslation("create"), VaadinIcon.USER_CHECK.create());
		submit.addClickShortcut(Key.ENTER);
		submit.getStyle().set("margin-top", "15px");
		// Button bar
		HorizontalLayout actions = new HorizontalLayout();
		actions.add(submit);

		formLayout.add(actions);

		add(formLayout);
		setAlignItems(Alignment.CENTER);
	}

	public Observable<League> validSubmit() {
		return RxUtil.clicks(submit)
				// Validate and don't emit if invalid
				.filter(ignored -> binder.validate().isOk())
				// Map to actual User object
				.map(ignored -> binder.getBean());
	}

	public void setErrorMap(Map<String, String> errorMap) {
		VaadinUtil.setErrorMap(binder, errorMap);
	}

	public void reset() {
		League bean = new League();
		bean.setTemplate(Template.UEFA_EURO_2024);
		binder.setBean(bean);
	}
}
