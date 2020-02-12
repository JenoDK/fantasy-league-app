package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.Map;

import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.enums.Template;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import io.reactivex.Observable;

public class LeagueForm extends HorizontalLayout {

	private VerticalLayout fieldLayout;
	private TextField nameField;
	private ComboBox<Template> templateCombobox;

	private Button submit;

	private BeanValidationBinder<League> binder = new BeanValidationBinder<>(League.class);

	public LeagueForm() {
		super();
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(nameField).bind("name");
		binder.forField(templateCombobox).bind("template");
		reset();
	}

	private void initLayout() {
		addClassNames("add-new-form");
		setSizeFull();
		setMargin(false);
		setSpacing(false);

		fieldLayout = new VerticalLayout();
		fieldLayout.setMargin(false);
		fieldLayout.setSpacing(true);

		nameField = new TextField();
//		nameField.addClassName(ValoTheme.TEXTFIELD_TINY);
		nameField.setPlaceholder("Name");

		templateCombobox = new ComboBox<>();
		templateCombobox.addClassName("no-required-indicator");
//		templateCombobox.addClassName(ValoTheme.COMBOBOX_TINY);
		templateCombobox.setPlaceholder("Template");
		templateCombobox.setItems(Template.values());
		templateCombobox.setItemLabelGenerator(Template::getName);
		// TODO
//		templateCombobox.setItemIconGenerator(temp -> new ThemeResource(temp.getIconPath()));

		fieldLayout.add(nameField);
		fieldLayout.add(templateCombobox);
		add(fieldLayout);

		submit = new CustomButton(getTranslation("create"), VaadinIcon.USER_CHECK.create());
		submit.addClickShortcut(Key.ENTER);
		add(submit);

//		setComponentAlignment(submit, Alignment.BOTTOM_RIGHT);
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
		bean.setTemplate(Template.FIFA_WORLD_CUP_2018);
		binder.setBean(bean);
	}
}
