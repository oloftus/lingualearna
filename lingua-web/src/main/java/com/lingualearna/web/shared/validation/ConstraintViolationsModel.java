package com.lingualearna.web.shared.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstraintViolationsModel {

	private List<String> globalErrors;
	private Map<String, List<String>> fieldErrors;

	public ConstraintViolationsModel() {

		globalErrors = new ArrayList<>();
		fieldErrors = new HashMap<>();
	}

	public List<String> getGlobalErrors() {

		return globalErrors;
	}

	public Map<String, List<String>> getFieldErrors() {

		return fieldErrors;
	}

	public void addGlobalError(String error) {

		globalErrors.add(error);
	}

	public void addFieldError(String fieldName, String error) {

		if (fieldErrors.get(fieldName) == null) {
			List<String> errorList = new ArrayList<>();
			fieldErrors.put(fieldName, errorList);
		}

		fieldErrors.get(fieldName).add(error);
	}

	public void addFieldErrors(String fieldName, List<String> errors) {

		fieldErrors.put(fieldName, errors);
	}
}
