package com.lingualearna.web.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

    private static final long serialVersionUID = -4621894928119661025L;

    private List<String> globalErrors;
    private List<FieldValidationError> fieldErrors;

    public ValidationException() {

        globalErrors = new ArrayList<>();
        fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String fieldName, String errorMessage) {

        FieldValidationError error = new FieldValidationError(fieldName, errorMessage);
        fieldErrors.add(error);
    }

    public void addGlobalError(String errorMessage) {

        globalErrors.add(errorMessage);
    }

    public List<FieldValidationError> getFieldErrors() {

        return fieldErrors;
    }

    public List<String> getGlobalErrors() {

        return globalErrors;
    }
}
