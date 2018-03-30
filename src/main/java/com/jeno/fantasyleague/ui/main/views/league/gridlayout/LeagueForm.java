package com.jeno.fantasyleague.ui.main.views.league.gridlayout;

import com.jeno.fantasyleague.model.leaguetemplates.Template;
import com.jeno.fantasyleague.util.RxUtil;
import com.jeno.fantasyleague.util.VaadinUtil;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.Observable;

import java.util.Map;

public class LeagueForm extends HorizontalLayout {

	private VerticalLayout fieldLayout;
	private TextField nameField;
	private ComboBox<Template> templateCombobox;

	private Button submit;

	private BeanValidationBinder<LeagueBean> binder = new BeanValidationBinder<>(LeagueBean.class);

	public LeagueForm() {
		super();
		initLayout();
		initBinder();
	}

	private void initBinder() {
		binder.forField(nameField).bind("name");
		binder.forField(templateCombobox).bind("template");
		binder.setBean(new LeagueBean());
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

		submit = new Button("Submit", VaadinIcons.USER_CHECK);
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addStyleName(ValoTheme.BUTTON_TINY);
		submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		addComponent(submit);

		setComponentAlignment(submit, Alignment.BOTTOM_RIGHT);
	}

	public Observable<LeagueBean> validSubmit() {
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
		binder.setBean(new LeagueBean());
	}
}
