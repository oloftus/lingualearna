package com.lingualearna.web.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.dao.GenericDao;
import com.lingualearna.web.exception.ConfigurationException;

@Component
public class UniqueWithinContextValidator implements ConstraintValidator<UniqueWithinContext, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(UniqueWithinContextValidator.class);

    private String namedQuery;
    private String contextParam;
    private String contextProperty;
    private String uniqueParam;
    private String uniqueProperty;

    @Autowired
    private GenericDao dao;

    private Object getPropertyValue(Object value, String propertyName) {

        try {
            return PropertyUtils.getProperty(value, propertyName);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = String.format("%s validator incorrectly setup.", this.getClass().getName());
            ConfigurationException exception = new ConfigurationException(message, cause);
            LOG.error(message, exception);
            throw exception;
        }
    }

    @Override
    public void initialize(UniqueWithinContext constraintAnnotation) {

        namedQuery = constraintAnnotation.namedQuery();
        contextParam = constraintAnnotation.contextParam();
        contextProperty = constraintAnnotation.contextProperty();
        uniqueParam = constraintAnnotation.uniqueParam();
        uniqueProperty = constraintAnnotation.uniqueProperty();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object contextPropertyValue = getPropertyValue(value, contextProperty);
        Object uniquePropertyValue = getPropertyValue(value, uniqueProperty);

        long count = dao.doUntypedQueryWithParams(namedQuery, Pair.of(uniqueParam, uniquePropertyValue),
                Pair.of(contextParam, contextPropertyValue));
        return count == 0;
    }
}
