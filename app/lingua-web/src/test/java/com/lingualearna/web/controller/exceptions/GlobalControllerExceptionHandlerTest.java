package com.lingualearna.web.controller.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.controller.model.ConstraintViolations;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    private static final String FIELD_ERROR_MESSAGE = "fieldErrorMessage";
    private static final String GLOBAL_ERROR_MESSAGE_1 = "globalErrorMessage1";
    private static final String GLOBAL_ERROR_MESSAGE_2 = "globalErrorMessage2";
    private static final String FIELD_NAME = "propertyPath";
    private static final String EMPTY_STRING = "";

    private ConstraintViolations returnedViolations;

    @Mock
    private ConstraintViolationException cve;

    @Mock
    private HttpServletRequest request;

    private GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    @Test
    public void testHandleConstraintViolations() {

        givenTheViolationsAreSetup();
        whenICallHandleConstraintViolations();
        thenTheViolationsAreCorrectlyHandled();
    }

    private void thenTheViolationsAreCorrectlyHandled() {

        assertTrue(returnedViolations.getFieldErrors().size() == 1);
        assertEquals(returnedViolations.getFieldErrors().get(FIELD_NAME).iterator().next(), FIELD_ERROR_MESSAGE);
        assertTrue(returnedViolations.getGlobalErrors().size() == 2);
        assertTrue(returnedViolations.getGlobalErrors().contains(GLOBAL_ERROR_MESSAGE_1));
        assertTrue(returnedViolations.getGlobalErrors().contains(GLOBAL_ERROR_MESSAGE_2));
    }

    private void whenICallHandleConstraintViolations() {

        returnedViolations = handler.handleConstraintViolations(request, cve);
    }

    private void givenTheViolationsAreSetup() {

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(createGlobalViolationWithNullPropertyPath());
        violations.add(createGlobalViolationWithEmptyPropertyPath());
        violations.add(createFieldViolation());
        when(cve.getConstraintViolations()).thenReturn(violations);
    }

    private ConstraintViolation<?> createFieldViolation() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getPropertyPath().toString()).thenReturn(FIELD_NAME);
        when(violation.getMessage()).thenReturn(FIELD_ERROR_MESSAGE);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithNullPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(null);
        when(violation.getMessage()).thenReturn(GLOBAL_ERROR_MESSAGE_1);
        return violation;
    }

    private ConstraintViolation<?> createGlobalViolationWithEmptyPropertyPath() {

        ConstraintViolation<?> violation = mock(ConstraintViolation.class, RETURNS_DEEP_STUBS);
        when(violation.getMessage()).thenReturn(GLOBAL_ERROR_MESSAGE_2);
        when(violation.getPropertyPath().toString()).thenReturn(EMPTY_STRING);
        return violation;
    }
}
