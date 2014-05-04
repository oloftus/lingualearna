package com.lingualearna.web.notes;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.util.ConfigurationException;

public class MinimumOnePropertyNotEmptyValidator implements ConstraintValidator<MinimumOnePropertyNotEmpty, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(MinimumOnePropertyNotEmptyValidator.class);
    private static final String BLANK = "";

    private String[] propertyNames;

    @Override
    public void initialize(MinimumOnePropertyNotEmpty constraintAnnotation) {

        propertyNames = constraintAnnotation.propertyNames();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        try {
            for (String propertyName : propertyNames) {
                if (BeanUtils.getProperty(object, propertyName) != null &&
                        !BeanUtils.getProperty(object, propertyName).trim().equals(BLANK)) {
                    return true;
                }
            }
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = "MinimumOnePropertyNotEmpty validator incorrectly setup";
            ConfigurationException exception = new ConfigurationException(message, cause);
            LOG.error(message, exception);
            throw exception;
        }

        return false;
    }
}
