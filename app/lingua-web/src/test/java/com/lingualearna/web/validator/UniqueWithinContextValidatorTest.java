package com.lingualearna.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.GenericDao;

@RunWith(MockitoJUnitRunner.class)
public class UniqueWithinContextValidatorTest {

    public static class ObjectUnderValidation {

        public String getContextProperty() {

            return "contextProperty";
        }

        public String getOwnIdProperty() {

            return "ownIdProperty";
        }

        public String getUniqueProperty() {

            return "uniqueProperty";
        }
    }

    private static final String OWN_ID_PARAM = "ownIdParam";
    private static final String OWN_ID_PROPERTY = "ownIdProperty";
    private static final String CONTEXT_PARAM = "contextParam";
    private static final String CONTEXT_PROPERTY = "contextProperty";
    private static final String UNIQUE_PARAM = "uniquePrarm";
    private static final String UNIQUE_PROPERTY = "uniqueProperty";
    private static final String NAMED_QUERY = "namedQuery";
    private static final UniqueWithinContext ANNOTATION;

    static {

        ANNOTATION = new UniqueWithinContext() {

            @Override
            public Class<? extends Annotation> annotationType() {

                return UniqueWithinContext.class;
            }

            @Override
            public String contextParam() {

                return CONTEXT_PARAM;
            }

            @Override
            public String contextProperty() {

                return CONTEXT_PROPERTY;
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
            public String namedQuery() {

                return NAMED_QUERY;
            }

            @Override
            public String ownIdParam() {

                return OWN_ID_PARAM;
            }

            @Override
            public String ownIdProperty() {

                return OWN_ID_PROPERTY;
            }

            @Override
            public Class<? extends Payload>[] payload() {

                return null;
            }

            @Override
            public String uniqueParam() {

                return UNIQUE_PARAM;
            }

            @Override
            public String uniqueProperty() {

                return UNIQUE_PROPERTY;
            }
        };
    }

    @Mock
    private GenericDao dao;

    @Mock
    private ConstraintValidatorContext context;

    private boolean validationResult;
    private ObjectUnderValidation objectUnderValidation = new ObjectUnderValidation();

    @InjectMocks
    private UniqueWithinContextValidator validator = new UniqueWithinContextValidator();

    private void givenTheValueIsNotUnique() {

        setupExistingObjects(1);
    }

    private void givenTheValueIsUnique() {

        setupExistingObjects(0);
    }

    @Before
    public void setup() {

        validator.initialize(ANNOTATION);
    }

    @SuppressWarnings("unchecked")
    private void setupExistingObjects(long existingObjectCount) {

        List<Object> resultsList = new ArrayList<>();
        resultsList.add(existingObjectCount);
        when(dao.doUntypedQueryAsListWithParams(NAMED_QUERY, Pair.of(UNIQUE_PARAM, UNIQUE_PROPERTY),
                Pair.of(CONTEXT_PARAM, CONTEXT_PROPERTY), Pair.of(OWN_ID_PARAM, OWN_ID_PROPERTY)))
                .thenReturn(resultsList);
    }

    @Test
    public void testInvalidObjectPassesValidation() {

        givenTheValueIsNotUnique();
        whenIValidateAnObject();
        thenTheObjectFailsValidation();
    }

    @Test
    public void testValidObjectPassesValidation() {

        givenTheValueIsUnique();
        whenIValidateAnObject();
        thenTheObjectPassesValidation();
    }

    private void thenTheObjectFailsValidation() {

        assertFalse(validationResult);
    }

    private void thenTheObjectPassesValidation() {

        assertTrue(validationResult);
    }

    private void whenIValidateAnObject() {

        validationResult = validator.isValid(objectUnderValidation, context);
    }
}
