package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class NonNullValidator<T> implements Validator<T> {

	private final String errorMessage;

	public NonNullValidator(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public ValidationResult apply(T value, ValueContext context) {
		return value != null ? ValidationResult.ok() : ValidationResult.error(errorMessage);
	}
}
