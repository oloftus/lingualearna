package com.lingualearna.web.controller.exceptions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingualearna.web.controller.model.ConstraintViolations;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    private static final String FIELD_NAME_1 = "fieldName1";
    private static final String FIELD_NAME_2 = "fieldName2";
    private static final String ERROR_MESSAGE_1 = "errorMessage1";
    private static final String ERROR_MESSAGE_2 = "errorMessage2";
    private static final String ERROR_MESSAGE_3 = "errorMessage3";
    private static final String ERROR_MESSAGE_4 = "errorMessage4";
    private static final String FIELD_ERROR_MESSAGE = "fieldErrorMessage";
    private static final String EMPTY_STRING = "";

    @Mock
    private HttpServletRequest request;

    @Mock
    private FieldError fieldError;

    @Mock
    private ObjectError globalError;

    @Mock
    private BindingResult methodArgumentErrors;

    @Mock
    private ConstraintViolationException cve;

    @Mock
    private MethodArgumentNotValidException manve;

    @Mock
    private ValidationException ve;

    private ConstraintViolations returnedViolations;

    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    private ConstraintViolation<?> createFieldViolation() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getPropertyPath().toString()).thenReturn(FIELD_NAME_1);
        when(violation.getMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWhichDuplicatesFieldViolation() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(null);
        when(violation.getMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithEmptyPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getPropertyPath().toString()).thenReturn(EMPTY_STRING);
        when(violation.getMessage()).thenReturn(ERROR_MESSAGE_1);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithNullPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(null);
        when(violation.getMessage()).thenReturn(ERROR_MESSAGE_2);
        return violation;
    }

    private void givenTheConstraintViolationExceptionIsSetup() {

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(createGlobalViolationWhichDuplicatesFieldViolation());
        violations.add(createGlobalViolationWithEmptyPropertyPath());
        violations.add(createGlobalViolationWithNullPropertyPath());
        violations.add(createFieldViolation());

        when(cve.getConstraintViolations()).thenReturn(violations);
    }

    private void givenTheMethodArgumentNotValidExceptionIsSetup() {

        List<FieldError> fieldErrors = Lists.newArrayList(fieldError);
        List<ObjectError> globalErrors = Lists.newArrayList(globalError);

        when(fieldError.getField()).thenReturn(FIELD_NAME_1);
        when(fieldError.getDefaultMessage()).thenReturn(ERROR_MESSAGE_1);
        when(globalError.getDefaultMessage()).thenReturn(ERROR_MESSAGE_2);

        when(methodArgumentErrors.getFieldErrors()).thenReturn(fieldErrors);
        when(methodArgumentErrors.getGlobalErrors()).thenReturn(globalErrors);

        when(manve.getBindingResult()).thenReturn(methodArgumentErrors);
    }

    private void givenTheValidationExceptionIsSetup() {

        FieldValidationError fieldError1 = new FieldValidationError(FIELD_NAME_1, ERROR_MESSAGE_1);
        FieldValidationError fieldError2 = new FieldValidationError(FIELD_NAME_2, ERROR_MESSAGE_2);
        ArrayList<FieldValidationError> fieldErrors = Lists.newArrayList(fieldError1, fieldError2);
        ArrayList<String> globalErrors = Lists.newArrayList(ERROR_MESSAGE_3, ERROR_MESSAGE_4);

        when(ve.getFieldErrors()).thenReturn(fieldErrors);
        when(ve.getGlobalErrors()).thenReturn(globalErrors);
    }

    @Test
    public void testHandleControllerModelConstraintViolations() {

        givenTheMethodArgumentNotValidExceptionIsSetup();
        whenICallHandleControllerModelConstraintViolations();
        thenTheControllerModelViolationsAreCorrectlyHandled();
    }

    @Test
    public void testHandleEntityConstraintViolations() {

        givenTheConstraintViolationExceptionIsSetup();
        whenICallHandleEntityConstraintViolations();
        thenTheEntityViolationsAreDeduplicatedAndReturned();
    }

    @Test
    public void testHandleValidationException() {

        givenTheValidationExceptionIsSetup();
        whenICallHandleValidtionException();
        thenTheValidationErrorsAreCorrectlyHandled();
    }

    private void thenTheControllerModelViolationsAreCorrectlyHandled() {

        HashMap<String, List<String>> expectedFieldErrors = Maps.newHashMap();
        expectedFieldErrors.put(FIELD_NAME_1, Lists.newArrayList(ERROR_MESSAGE_1));

        List<String> expectedGlobalErrors = Lists.newArrayList(ERROR_MESSAGE_2);

        assertEquals(expectedFieldErrors, returnedViolations.getFieldErrors());
        assertEquals(expectedGlobalErrors, returnedViolations.getGlobalErrors());

    }

    private void thenTheEntityViolationsAreDeduplicatedAndReturned() {

        HashMap<String, List<String>> expectedFieldErrors = Maps.newHashMap();
        expectedFieldErrors.put(FIELD_NAME_1, Lists.newArrayList(FIELD_ERROR_MESSAGE));

        List<String> expectedGlobalErrors = Lists.newArrayList(ERROR_MESSAGE_1, ERROR_MESSAGE_2);

        assertEquals(expectedFieldErrors, returnedViolations.getFieldErrors());
        assertEquals(expectedGlobalErrors, returnedViolations.getGlobalErrors());
    }

    private void thenTheValidationErrorsAreCorrectlyHandled() {

        HashMap<String, List<String>> expectedFieldErrors = Maps.newHashMap();
        expectedFieldErrors.put(FIELD_NAME_1, Lists.newArrayList(ERROR_MESSAGE_1));
        expectedFieldErrors.put(FIELD_NAME_2, Lists.newArrayList(ERROR_MESSAGE_2));

        List<String> expectedGlobalErrors = Lists.newArrayList(ERROR_MESSAGE_3, ERROR_MESSAGE_4);

        assertEquals(expectedFieldErrors, returnedViolations.getFieldErrors());
        assertEquals(expectedGlobalErrors, returnedViolations.getGlobalErrors());
    }

    private void whenICallHandleControllerModelConstraintViolations() {

        returnedViolations = handler.handleControllerModelConstraintViolations(request, manve);
    }

    private void whenICallHandleEntityConstraintViolations() {

        returnedViolations = handler.handleEntityConstraintViolations(request, cve);
    }

    private void whenICallHandleValidtionException() {

        returnedViolations = handler.handleValidationExceptions(request, ve);
    }
}
