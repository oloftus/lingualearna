package com.lingualearna.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.validator.MinimumOnePropertyNotEmpty;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmptyValidator;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmptyValidatorTestHelpers.AllFieldsNotNull;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmptyValidatorTestHelpers.AllFieldsNull;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmptyValidatorTestHelpers.OneFieldNotNull;
import com.lingualearna.web.validator.MinimumOnePropertyNotEmptyValidatorTestHelpers.TypeUnderValidation;

@RunWith(MockitoJUnitRunner.class)
public class MinimumOnePropertyNotEmptyValidatorTest {

    private static final String[] PROPERTY_NAMES = { "property1", "property2" };

    private static final MinimumOnePropertyNotEmpty ANNOTATION;

    static {

        ANNOTATION = new MinimumOnePropertyNotEmpty() {

            @Override
            public Class<? extends Annotation> annotationType() {

                return MinimumOnePropertyNotEmpty.class;
            }

            @Override
            public Class<?>[] groups() {

                return null;
            }

            @Override
            public String message() {

                return null;
            }

            @Override
            public Class<? extends Payload>[] payload() {

                return null;
            }

            @Override
            public String[] propertyNames() {

                return PROPERTY_NAMES;
            }
        };
    }

    @Mock
    private ConstraintValidatorContext context;

    private TypeUnderValidation validObjAllFieldsNotNull = new AllFieldsNotNull();
    private TypeUnderValidation validObjOneFieldNotNull = new OneFieldNotNull();
    private TypeUnderValidation invalidObjAllFieldsNull = new AllFieldsNull();

    @InjectMocks
    private MinimumOnePropertyNotEmptyValidator validator = new MinimumOnePropertyNotEmptyValidator();

    @Before
    public void setup() {

        validator.initialize(ANNOTATION);
    }

    @Test
    public void testAllFieldsNotNullPassesValidation() {

        theObjectPassesValidation(validObjAllFieldsNotNull);
    }

    @Test
    public void testAllOneFieldNotNullFailsValidation() {

        theObjectFailsValidation(invalidObjAllFieldsNull);
    }

    @Test
    public void testOneFieldNotNullPassesValidation() {

        theObjectPassesValidation(validObjOneFieldNotNull);
    }

    private void theObjectFailsValidation(TypeUnderValidation typeUnderValidation) {

        assertFalse(validator.isValid(typeUnderValidation, context));
    }

    private void theObjectPassesValidation(TypeUnderValidation typeUnderValidation) {

        assertTrue(validator.isValid(typeUnderValidation, context));
    }
}
