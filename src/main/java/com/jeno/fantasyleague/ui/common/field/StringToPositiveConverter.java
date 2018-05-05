package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;

public class StringToPositiveConverter extends StringToIntegerConverter {

	public StringToPositiveConverter(String errorMessage) {
		super(errorMessage);
	}

	public StringToPositiveConverter(Integer emptyValue, String errorMessage) {
		super(emptyValue, errorMessage);
	}

	@Override
	public Result<Integer> convertToModel(String value, ValueContext context) {
		return super.convertToModel(value, context).flatMap(result -> {
			if (result == null || result < 0) {
				return Result.error(getErrorMessage());
			} else {
				return Result.ok(result);
			}
		});
	}
}
