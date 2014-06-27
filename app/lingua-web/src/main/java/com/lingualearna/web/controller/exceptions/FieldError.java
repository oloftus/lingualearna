package com.lingualearna.web.controller.exceptions;

public class FieldError {

    private String fieldName;
    private String errorMessage;

    public FieldError(String fieldName, String errorMessage) {

        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FieldError other = (FieldError) obj;
        if (errorMessage == null) {
            if (other.errorMessage != null) {
                return false;
            }
        }
        else if (!errorMessage.equals(other.errorMessage)) {
            return false;
        }
        if (fieldName == null) {
            if (other.fieldName != null) {
                return false;
            }
        }
        else if (!fieldName.equals(other.fieldName)) {
            return false;
        }
        return true;
    }

    public String getErrorMessage() {

        return errorMessage;
    }

    public String getFieldName() {

        return fieldName;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        return result;
    }
}
