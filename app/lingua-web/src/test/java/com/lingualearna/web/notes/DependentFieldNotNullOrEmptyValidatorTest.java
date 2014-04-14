package com.lingualearna.web.notes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.InvalidPropertyNotNullDependentBlank;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.InvalidPropertyNotNullDependentNull;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.TypeUnderValidation;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.ValidPropertyBlankDependentNull;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.ValidPropertyNotNullDependentNotNull;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.ValidPropertyNullDependentNotNull;
import com.lingualearna.web.notes.DependentFieldNotNullOrEmptyValidatorTestHelpers.ValidPropertyNullDependentNull;

@RunWith(MockitoJUnitRunner.class)
public class DependentFieldNotNullOrEmptyValidatorTest {

    private static final String PROPERTY_NAME = "property";
    private static final String DEPENDENT_PROPERTY_NAME = "dependentProperty";
    private static final DependentPropertyNotNullOrEmpty ANNOTATION;

    static {

        ANNOTATION = new DependentPropertyNotNullOrEmpty() {

            @Override
            public Class<? extends Annotation> annotationType() {

                return DependentPropertyNotNullOrEmpty.class;
            }

            @Override
            public String dependentPropertyName() {

                return DEPENDENT_PROPERTY_NAME;
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
            public String propertyName() {

                return PROPERTY_NAME;
            }
        };
    }

    private TypeUnderValidation validObjPropertyNullDependentNull = new ValidPropertyNullDependentNull();
    private TypeUnderValidation validObjPropertyNullDependentNotNull = new ValidPropertyNullDependentNotNull();
    private TypeUnderValidation validObjPropertyNotNullDependentNotNull = new ValidPropertyNotNullDependentNotNull();
    private TypeUnderValidation validObjPropertyBlankDependentNull = new ValidPropertyBlankDependentNull();
    private TypeUnderValidation invalidObjPropertyNotNullDependentBlank = new InvalidPropertyNotNullDependentBlank();
    private TypeUnderValidation invalidObjPropertyNotNullDependentNull = new InvalidPropertyNotNullDependentNull();

    private boolean validationResult;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

    private final DependentPropertyNotNullOrEmptyValidator validator = new DependentPropertyNotNullOrEmptyValidator();

    private void andTheObjectFailsValidation() {

        assertFalse(validationResult);
    }

    @Before
    public void setup() {

        validator.initialize(ANNOTATION);
    }

    @Test
    public void testInvalidObjPropertyNotNullDependentBlankTriggersViolation() {

        whenIValidateTheObject(invalidObjPropertyNotNullDependentBlank);
        thenTheErrorObjectIsCorrectlySetup();
        andTheObjectFailsValidation();
    }

    @Test
    public void testObjPropertyBlankDependentNullPassesValidation() {

        whenIValidateTheObject(validObjPropertyBlankDependentNull);
        thenTheObjectPassesValidation();
    }

    @Test
    public void testObjPropertyNotNullDependentNotNullPassesValidation() {

        whenIValidateTheObject(validObjPropertyNotNullDependentNotNull);
        thenTheObjectPassesValidation();
    }

    @Test
    public void testObjPropertyNotNullDependentNullTriggersViolation() {

        whenIValidateTheObject(invalidObjPropertyNotNullDependentNull);
        thenTheErrorObjectIsCorrectlySetup();
        andTheObjectFailsValidation();
    }

    @Test
    public void testObjPropertyNullDependentNotNullPassesValidation() {

        whenIValidateTheObject(validObjPropertyNullDependentNotNull);
    }

    @Test
    public void testObjPropertyNullDependentNullPassesValidation() {

        whenIValidateTheObject(validObjPropertyNullDependentNull);
    }

    private void thenTheErrorObjectIsCorrectlySetup() {

        verify(context).buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(DEPENDENT_PROPERTY_NAME)
                .addConstraintViolation();

    }

    private void thenTheObjectPassesValidation() {

        assertTrue(validationResult);
    }

    private void whenIValidateTheObject(Object obj) {

        validationResult = validator.isValid(obj, context);
    }
}
