package com.jeno.fantasyleague.util;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class VaadinUtil {

	public static void addStyles(Style style, String styles) {
		Arrays.stream(styles.split(";"))
				.map(individualStyle -> individualStyle.split(":"))
				.filter(s -> s.length == 2)
				.forEach(s -> style.set(s[0].trim(), s[1].trim()));
	}

	public static Validator<String> getPasswordsMatchValidator(PasswordField passwordField) {
		return (Validator<String>) (value, context) -> {
			if (!value.isEmpty() && !passwordField.getOptionalValue().equals(Optional.ofNullable(value))) {
				return ValidationResult.error("Needs to be the same as password");
			}
			return ValidationResult.ok();
		};
	}

	public static String getRootRequestURL() {
		String completeUrl = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURL().toString();
		String uri = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURI();
		return completeUrl.replace(uri, "");
	}

	public static <T> Map<String, String> setErrorMap(BeanValidationBinder<T> binder, Map<String, String> errorMap) {
		Map<String, String> errorsWithoutBinding = Maps.newHashMap();
		errorMap.entrySet().forEach(entry -> {
			Optional<Binder.Binding<T, ?>> binding = binder.getBinding(entry.getKey());
			if (binding.isPresent()) {
				((HasValidation) binding.get().getField()).setErrorMessage(entry.getValue());
			} else {
				errorsWithoutBinding.put(entry.getKey(), entry.getValue());
			}
		});
		return errorsWithoutBinding;
	}

	public static void logout() {
		String logoutUrl = VaadinService.getCurrentRequest().getContextPath() + "/logout";
		UI.getCurrent().getPage().setLocation(logoutUrl);
	}

	public static Locale findLocale() {
		Locale l = null;
		if (l == null && UI.getCurrent() != null) {
			l = UI.getCurrent().getLocale();
		}
		if (l == null) {
			l = Locale.getDefault();
		}
		return l;
	}

}
