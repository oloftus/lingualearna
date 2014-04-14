package com.lingualearna.web.notes;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.util.ConfigurationException;

public class DependentPropertyNotNullOrEmptyValidator implements
        ConstraintValidator<DependentPropertyNotNullOrEmpty, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(DependentPropertyNotNullOrEmptyValidator.class);

    private String propertyName;
    private String dependentPropertyName;

    private void addViolation(ConstraintValidatorContext context) {

        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(dependentPropertyName)
                .addConstraintViolation();
    }

    @Override
    public void initialize(DependentPropertyNotNullOrEmpty constraintAnnotation) {

        propertyName = constraintAnnotation.propertyName();
        dependentPropertyName = constraintAnnotation.dependentPropertyName();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        try {
            if (propertyIsSetButDependentIsNotSet(object)) {
                addViolation(context);
                return false;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = "DependentPropertyNotNull validator incorrectly setup";
            ConfigurationException exception = new ConfigurationException(message, cause);
            LOG.error(message, exception);
            throw exception;
        }

        return true;
    }

    private boolean propertyIsSetButDependentIsNotSet(Object object) throws IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {

        return (BeanUtils.getProperty(object, propertyName) != null &&
                !BeanUtils.getProperty(object, propertyName).trim().isEmpty())
                &&
                (BeanUtils.getProperty(object, dependentPropertyName) == null ||
                BeanUtils.getProperty(object, dependentPropertyName).trim().isEmpty());
    }
}
