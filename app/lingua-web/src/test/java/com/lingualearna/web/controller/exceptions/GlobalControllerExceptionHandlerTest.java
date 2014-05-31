package com.lingualearna.web.controller.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.lingualearna.web.controller.model.ConstraintViolations;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    private static final String FIELD_ERROR_MESSAGE = "fieldErrorMessage";
    private static final String GLOBAL_ERROR_MESSAGE_1 = "globalErrorMessage1";
    private static final String GLOBAL_ERROR_MESSAGE_2 = "globalErrorMessage2";
    private static final String FIELD_NAME = "propertyPath";
    private static final String EMPTY_STRING = "";

    @Mock
    private ConstraintViolationException cve;

    @Mock
    private MethodArgumentNotValidException manve;

    @Mock
    private HttpServletRequest request;

    private ConstraintViolations returnedViolations;

    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    private ConstraintViolation<?> createFieldViolation() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getPropertyPath().toString()).thenReturn(FIELD_NAME);
        when(violation.getMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithEmptyPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getMessage()).thenReturn(GLOBAL_ERROR_MESSAGE_2);
        when(violation.getPropertyPath().toString()).thenReturn(EMPTY_STRING);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithFieldErrorMessage() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getPropertyPath()).thenReturn(null);
        when(violation.getMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithNullPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(null);
        when(violation.getMessage()).thenReturn(GLOBAL_ERROR_MESSAGE_1);
        return violation;
    }

    private void givenTheControllerModelViolationsAreSetup() {

        BindingResult methodArgumentErrors = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);
        ObjectError globalError = mock(ObjectError.class);

        when(fieldError.getField()).thenReturn(FIELD_NAME);
        when(fieldError.getDefaultMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        when(globalError.getDefaultMessage()).thenReturn(GLOBAL_ERROR_MESSAGE_1);

        List<FieldError> fieldErrors = Lists.newArrayList(fieldError);
        List<ObjectError> globalErrors = Lists.newArrayList(globalError);

        when(methodArgumentErrors.getFieldErrors()).thenReturn(fieldErrors);
        when(methodArgumentErrors.getGlobalErrors()).thenReturn(globalErrors);
        when(manve.getBindingResult()).thenReturn(methodArgumentErrors);
    }

    private void givenTheEntityViolationsAreSetup() {

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(createGlobalViolationWithNullPropertyPath());
        violations.add(createGlobalViolationWithEmptyPropertyPath());
        violations.add(createFieldViolation());
        violations.add(createGlobalViolationWithFieldErrorMessage());
        when(cve.getConstraintViolations()).thenReturn(violations);
    }

    @Test
    public void testHandleControllerModelConstraintViolations() {

        givenTheControllerModelViolationsAreSetup();
        whenICallHandleControllerModelConstraintViolations();
        thenTheControllerModelViolationsAreCorrectlyHandled();
    }

    @Test
    public void testHandleEntityConstraintViolations() {

        givenTheEntityViolationsAreSetup();
        whenICallHandleEntityConstraintViolations();
        thenTheEntityViolationsAreCorrectlyHandled();
    }

    private void thenTheControllerModelViolationsAreCorrectlyHandled() {

        assertEquals(1, returnedViolations.getFieldErrors().size());
        assertEquals(1, returnedViolations.getFieldErrors().get(FIELD_NAME).size());
        assertEquals(FIELD_ERROR_MESSAGE, returnedViolations.getFieldErrors().get(FIELD_NAME).get(0));

        assertEquals(1, returnedViolations.getGlobalErrors().size());
        assertEquals(GLOBAL_ERROR_MESSAGE_1, returnedViolations.getGlobalErrors().get(0));
    }

    private void thenTheEntityViolationsAreCorrectlyHandled() {

        assertEquals(1, returnedViolations.getFieldErrors().size());
        assertEquals(returnedViolations.getFieldErrors().get(FIELD_NAME).iterator().next(), FIELD_ERROR_MESSAGE);

        assertEquals(2, returnedViolations.getGlobalErrors().size());
        assertTrue(returnedViolations.getGlobalErrors().contains(GLOBAL_ERROR_MESSAGE_1));
        assertTrue(returnedViolations.getGlobalErrors().contains(GLOBAL_ERROR_MESSAGE_2));
    }

    private void whenICallHandleControllerModelConstraintViolations() {

        returnedViolations = handler.handleControllerModelConstraintViolations(request, manve);
    }

    private void whenICallHandleEntityConstraintViolations() {

        returnedViolations = handler.handleEntityConstraintViolations(request, cve);
    }
}
