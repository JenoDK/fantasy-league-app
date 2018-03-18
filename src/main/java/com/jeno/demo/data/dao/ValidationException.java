package com.jeno.demo.data.dao;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 3499649702601551118L;

	private Map<String, String> errorMap;

	public ValidationException(String field, String message) {
		super("ErrorMap: " + field + " " + message);
		this.errorMap = new HashMap<String, String>();
		this.errorMap.put(field, message);
	}

	public ValidationException(Map<String, String> errorMap) {
		super("ErrorMap: " + errorMap.toString());
		this.errorMap = errorMap;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

}
