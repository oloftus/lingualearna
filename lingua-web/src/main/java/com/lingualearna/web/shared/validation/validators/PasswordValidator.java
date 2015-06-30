package com.lingualearna.web.shared.validation.validators;

import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.validation.ValidationException;

@Component
public class PasswordValidator {

    private static final int PASSWORD_MIN_LENGTH = 10;
    private static final int PASSWORD_MAX_LENGTH = 45;
    private static final String PASSWORD_FIELD_NAME = "password";
    private static final String PASSWORD_LENGTH_ERROR_TEMPLATE = "Passwords must be between %s and %s characters in length";
    private static final String PASSWORD_LENGTH_ERROR = String.format(PASSWORD_LENGTH_ERROR_TEMPLATE,
            PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);

    public void validate(String password) {

        int passwordLength = password.length();

        if (!(PASSWORD_MIN_LENGTH <= passwordLength && passwordLength <= PASSWORD_MAX_LENGTH)) {
            ValidationException exception = new ValidationException();
            exception.addFieldError(PASSWORD_FIELD_NAME, PASSWORD_LENGTH_ERROR);
            throw exception;
        }
    }
}
