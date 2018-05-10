package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

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
