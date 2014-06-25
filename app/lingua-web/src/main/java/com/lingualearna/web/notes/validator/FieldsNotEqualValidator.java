package com.lingualearna.web.notes.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.util.ConfigurationException;

/**
 * Does not consider blank values to be equal to one another.
 */
public class FieldsNotEqualValidator implements ConstraintValidator<FieldsNotEqual, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(FieldsNotEqualValidator.class);

    private String[] propertyNames;

    @Override
    public void initialize(FieldsNotEqual constraintAnnotation) {

        propertyNames = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        Set<String> propertyValues = new HashSet<>();
        int numPropertiesChecked = 0;

        try {
            for (String propertyName : propertyNames) {
                String propertyValue = BeanUtils.getProperty(object, propertyName);

                if (propertyValue != null && propertyValue.trim().length() != 0) {
                    propertyValues.add(propertyValue);
                    numPropertiesChecked++;
                }

                if (propertyValues.size() != numPropertiesChecked) {
                    return false;
                }
            }
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = "FieldsNotEqualValidator validator incorrectly setup";
            ConfigurationException exception = new ConfigurationException(message, cause);
            LOG.error(message, exception);
            throw exception;
        }

        return true;
    }
}
