package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import java.util.Map;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.enums.Template;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
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
		addStyleNames("add-new-form");
		setSizeFull();
		setMargin(false);
		setSpacing(false);

		fieldLayout = new VerticalLayout();
		fieldLayout.setMargin(false);
		fieldLayout.setSpacing(true);

		nameField = new TextField();
		nameField.addStyleName(ValoTheme.TEXTFIELD_TINY);
		nameField.setPlaceholder("Name");

		templateCombobox = new ComboBox<>();
		templateCombobox.addStyleName("no-required-indicator");
		templateCombobox.addStyleName(ValoTheme.COMBOBOX_TINY);
		templateCombobox.setPlaceholder("Template");
		templateCombobox.setItems(Template.values());
		templateCombobox.setItemCaptionGenerator(Template::getName);
		templateCombobox.setItemIconGenerator(temp -> new ThemeResource(temp.getIconPath()));

		fieldLayout.addComponent(nameField);
		fieldLayout.addComponent(templateCombobox);
		addComponent(fieldLayout);

		submit = new CustomButton(Resources.getMessage("create"), VaadinIcons.USER_CHECK);
		submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		addComponent(submit);

		setComponentAlignment(submit, Alignment.BOTTOM_RIGHT);
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
