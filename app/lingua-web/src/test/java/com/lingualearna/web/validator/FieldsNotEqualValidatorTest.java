package com.lingualearna.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.lingualearna.web.validator.FieldsNotEqualValidatorTestHelper.TypeUnderValidation;

public class FieldsNotEqualValidatorTest {

    private static final String PROPERTY_1 = "property1";
    private static final String PROPERTY_2 = "property2";
    private static final FieldsNotEqual ANNOTATION;

    static {

        ANNOTATION = new FieldsNotEqual() {

            @Override
            public Class<? extends Annotation> annotationType() {

                return FieldsNotEqual.class;
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
            public String[] value() {

                return new String[] { PROPERTY_1, PROPERTY_2 };
            }
        };
    }

    private TypeUnderValidation invalidObjFieldsEqualFailsValidation = new FieldsNotEqualValidatorTestHelper.InvalidPropertiesEqual();
    private TypeUnderValidation validObjFieldsBlankPassesValidation = new FieldsNotEqualValidatorTestHelper.ValidPropertiesBlank();
    private TypeUnderValidation validObjPassesValidation = new FieldsNotEqualValidatorTestHelper.ValidPropertiesNotEqual();

    private boolean validationResult;

    @Mock
    private ConstraintValidatorContext context;

    private final FieldsNotEqualValidator validator = new FieldsNotEqualValidator();

    @Before
    public void setup() {

        validator.initialize(ANNOTATION);
    }

    @Test
    public void testInvalidObjFieldsEqualFailsValidation() {

        whenIValidateTheObject(invalidObjFieldsEqualFailsValidation);
        thenTheObjectFailsValidation();
    }

    @Test
    public void testValidObjFieldsBlankFailsValidation() {

        whenIValidateTheObject(validObjFieldsBlankPassesValidation);
        thenTheObjectPassesValidation();
    }

    @Test
    public void testValidObjPassesValidation() {

        whenIValidateTheObject(validObjPassesValidation);
        thenTheObjectPassesValidation();
    }

    private void thenTheObjectFailsValidation() {

        assertFalse(validationResult);
    }

    private void thenTheObjectPassesValidation() {

        assertTrue(validationResult);
    }

    private void whenIValidateTheObject(Object obj) {

        validationResult = validator.isValid(obj, context);
    }
}
