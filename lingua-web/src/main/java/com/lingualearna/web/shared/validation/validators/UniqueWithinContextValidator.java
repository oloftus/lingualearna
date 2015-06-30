package com.lingualearna.web.shared.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.data.GenericDao;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;
import com.lingualearna.web.shared.validation.annotations.UniqueWithinContext;

@Component
public class UniqueWithinContextValidator extends ObjectValidator implements
        ConstraintValidator<UniqueWithinContext, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(UniqueWithinContextValidator.class);

    private String namedQuery;
    private String contextParam;
    private String contextProperty;
    private String uniqueParam;
    private String uniqueProperty;
    private String ownIdParam;
    private String ownIdProperty;

    @Autowired
    private GenericDao dao;

    @Override
    protected Logger getLog() {

        return LOG;
    }

    @Override
    public void initialize(UniqueWithinContext constraintAnnotation) {

        namedQuery = constraintAnnotation.namedQuery();
        contextParam = constraintAnnotation.contextParam();
        contextProperty = constraintAnnotation.contextProperty();
        uniqueParam = constraintAnnotation.uniqueParam();
        uniqueProperty = constraintAnnotation.uniqueProperty();
        ownIdParam = constraintAnnotation.ownIdParam();
        ownIdProperty = constraintAnnotation.ownIdProperty();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object contextPropertyValue = getPropertyValue(value, contextProperty);
        Object uniquePropertyValue = getPropertyValue(value, uniqueProperty);
        Object ownIdPropertyValue = getPropertyValue(value, ownIdProperty);

        long count = dao.doUntypedQueryWithParams(namedQuery, Pair.of(uniqueParam, uniquePropertyValue),
                Pair.of(contextParam, contextPropertyValue), Pair.of(ownIdParam, ownIdPropertyValue));
        return count == 0;
    }
}
