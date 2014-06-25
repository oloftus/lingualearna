package com.lingualearna.web.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

    public class FieldError {

        private String fieldName;

        private String errorMessage;

        public FieldError(String fieldName, String errorMessage) {

            this.fieldName = fieldName;
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {

            return errorMessage;
        }

        public String getFieldName() {

            return fieldName;
        }
    }

    private static final long serialVersionUID = -4621894928119661025L;

    private List<String> globalErrors;
    private List<FieldError> fieldErrors;

    public ValidationException() {

        globalErrors = new ArrayList<>();
        fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String fieldName, String errorMessage) {

        FieldError error = new FieldError(fieldName, errorMessage);
        fieldErrors.add(error);
    }

    public void addGlobalError(String errorMessage) {

        globalErrors.add(errorMessage);
    }

    public List<FieldError> getFieldErrors() {

        return fieldErrors;
    }

    public List<String> getGlobalErrors() {

        return globalErrors;
    }
}
