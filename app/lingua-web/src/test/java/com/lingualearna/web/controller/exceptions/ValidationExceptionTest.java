package com.lingualearna.web.controller.exceptions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ValidationExceptionTest {

    private static final String FIELD_NAME_1 = "fieldName1";
    private static final String FIELD_NAME_2 = "fieldName2";
    private static final String ERROR_MESSAGE_1 = "errorMessage1";
    private static final String ERROR_MESSAGE_2 = "errorMessage2";

    private ArrayList<FieldValidationError> fieldErrors;
    private ArrayList<String> globalErrors = Lists.newArrayList(ERROR_MESSAGE_1, ERROR_MESSAGE_2);

    private ValidationException validationException;

    @Before
    public void setup() {

        validationException = new ValidationException();

        setupFieldErrors();
    }

    private void setupFieldErrors() {

        FieldValidationError fieldError1 = new FieldValidationError(FIELD_NAME_1, ERROR_MESSAGE_1);
        FieldValidationError fieldError2 = new FieldValidationError(FIELD_NAME_2, ERROR_MESSAGE_2);
        fieldErrors = Lists.newArrayList(fieldError1, fieldError2);
    }

    @Test
    public void testFieldErrorsGetAndSet() {

        whenIAddFieldErrorsToTheException();
        thenTheFieldErrorsAreAttachedToTheException();
    }

    @Test
    public void testGlobalErrorsGetAndSet() {

        whenIAddGlobalErrorsToTheException();
        thenTheGlobalErrorsAreAttachedToTheException();
    }

    private void thenTheFieldErrorsAreAttachedToTheException() {

        assertEquals(fieldErrors, validationException.getFieldErrors());
    }

    private void thenTheGlobalErrorsAreAttachedToTheException() {

        assertEquals(globalErrors, validationException.getGlobalErrors());
    }

    private void whenIAddFieldErrorsToTheException() {

        validationException.addFieldError(FIELD_NAME_1, ERROR_MESSAGE_1);
        validationException.addFieldError(FIELD_NAME_2, ERROR_MESSAGE_2);
    }

    private void whenIAddGlobalErrorsToTheException() {

        validationException.addGlobalError(ERROR_MESSAGE_1);
        validationException.addGlobalError(ERROR_MESSAGE_2);
    }
}
