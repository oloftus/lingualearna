package com.lingualearna.web.shared.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Equivalent in non-JSR-303 validators to ConstraintViolationException in
 * JSR-303 validators.
 * 
 * Scheduled for deletion once all validators are in JSR-303 format.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -4621894928119661025L;

    private List<String> globalErrors;
    private Map<String, List<String>> fieldErrors;

    public ValidationException() {

        globalErrors = new ArrayList<>();
        fieldErrors = new HashMap<>();
    }

    public void addFieldError(String fieldName, String errorMessage) {

        if (!fieldErrors.containsKey(fieldName)) {
            fieldErrors.put(fieldName, new ArrayList<String>());
        }
        fieldErrors.get(fieldName).add(errorMessage);
    }

    public void addGlobalError(String errorMessage) {

        globalErrors.add(errorMessage);
    }

    public Map<String, List<String>> getFieldErrors() {

        return fieldErrors;
    }

    public List<String> getGlobalErrors() {

        return globalErrors;
    }
}
