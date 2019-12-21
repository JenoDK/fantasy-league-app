package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

public class StringToPositiveIntegerConverter extends StringToIntegerConverter {

	public StringToPositiveIntegerConverter(String errorMessage) {
		super(errorMessage);
	}

	public StringToPositiveIntegerConverter(Integer emptyValue, String errorMessage) {
		super(emptyValue, errorMessage);
	}

	@Override
	public Result<Integer> convertToModel(String value, ValueContext context) {
		return super.convertToModel(value, context).flatMap(result -> {
			if (result == null || result < 0) {
				return Result.error(getErrorMessage(context));
			} else {
				return Result.ok(result);
			}
		});
	}

}
