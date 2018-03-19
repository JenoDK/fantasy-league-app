package com.jeno.demo.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.ui.AbstractField;

public class VaadinUtil {

	public static void addValidator(AbstractField field, Validator validator) {
		field.addValueChangeListener(event -> {
			ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));

			if (result.isError()) {
				UserError error = new UserError(result.getErrorMessage());
				field.setComponentError(error);
			} else {
				field.setComponentError(null);
			}
		});
	}

	public static String getRootRequestURL() {
		String completeUrl = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURL().toString();
		String uri = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().getRequestURI();
		return completeUrl.replace(uri, "");
	}
}
