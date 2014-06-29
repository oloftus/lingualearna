package com.lingualearna.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.GenericDao;

@RunWith(MockitoJUnitRunner.class)
public class UniqueValidatorTest {

    @Mock
    private GenericDao dao;

    @Mock
    private ConstraintValidatorContext context;

    private boolean validationResult;

    @InjectMocks
    private UniqueValidator validator = new UniqueValidator();

    private void givenTheValueIsNotUnique() {

        long existingObjectCount = 1;
        setupExistingObjects(existingObjectCount);
    }

    private void givenTheValueIsUnique() {

        long existingObjectCount = 0;
        setupExistingObjects(existingObjectCount);
    }

    @SuppressWarnings("unchecked")
    private void setupExistingObjects(long existingObjectCount) {

        List<Object> resultsList = new ArrayList<>();
        resultsList.add(existingObjectCount);
        when(dao.doUntypedQueryAsListWithParams(any(String.class), (Pair<String, ? extends Object>[]) anyVararg()))
                .thenReturn(resultsList);
    }

    @Test
    public void testInvalidObjectPassesValidation() {

        givenTheValueIsNotUnique();
        whenIValidateAValidObject();
        thenTheObjectFailsValidation();
    }

    @Test
    public void testValidObjectPassesValidation() {

        givenTheValueIsUnique();
        whenIValidateAValidObject();
        thenTheObjectPassesValidation();
    }

    private void thenTheObjectFailsValidation() {

        assertFalse(validationResult);
    }

    private void thenTheObjectPassesValidation() {

        assertTrue(validationResult);
    }

    private void whenIValidateAValidObject() {

        validationResult = validator.isValid("value", context);
    }
}
