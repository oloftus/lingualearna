package com.lingualearna.web.notes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import javax.validation.Payload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.InvalidPropertyNotNullDependentBlank;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.InvalidPropertyNotNullDependentNull;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.TypeUnderValidation;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.ValidPropertyBlankDependentNull;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.ValidPropertyNotNullDependentNotNull;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.ValidPropertyNullDependentNotNull;
import com.lingualearna.web.notes.DependentPropertyNotNullOrEmptyValidatorTestHelpers.ValidPropertyNullDependentNull;

@RunWith(MockitoJUnitRunner.class)
public class DependentPropertyNotNullOrEmptyValidatorTest {

    private static final String DEFAULT_CONSTRAINT_MESSAGE = "defaultConstraintMessage";
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

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    private final DependentPropertyNotNullOrEmptyValidator validator = new DependentPropertyNotNullOrEmptyValidator();

    private void andTheObjectFailsValidation() {

        assertFalse(validationResult);
    }

    private void givenTheConstraintViolationContextIsSetup() {

        when(context.getDefaultConstraintMessageTemplate()).thenReturn(DEFAULT_CONSTRAINT_MESSAGE);
        when(context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())).thenReturn(
                constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(PROPERTY_NAME)).thenReturn(
                nodeBuilderCustomizableContext);
    }

    @Before
    public void setup() {

        validator.initialize(ANNOTATION);
    }

    @Test
    public void testInvalidObjPropertyNotNullDependentBlankTriggersViolation() {

        givenTheConstraintViolationContextIsSetup();
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

        givenTheConstraintViolationContextIsSetup();
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

        verify(nodeBuilderCustomizableContext).addConstraintViolation();

        // verify(context).buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
        // .addPropertyNode(DEPENDENT_PROPERTY_NAME)
        // .addConstraintViolation();
    }

    private void thenTheObjectPassesValidation() {

        assertTrue(validationResult);
    }

    private void whenIValidateTheObject(Object obj) {

        validationResult = validator.isValid(obj, context);
    }
}
