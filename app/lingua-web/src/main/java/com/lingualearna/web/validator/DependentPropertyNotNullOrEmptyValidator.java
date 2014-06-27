package com.lingualearna.web.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

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

        String messageTemplate = context.getDefaultConstraintMessageTemplate();
        ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(messageTemplate);
        NodeBuilderCustomizableContext nbcc = violationBuilder.addPropertyNode(propertyName);
        nbcc.addConstraintViolation();
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
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = "DependentPropertyNotNull validator incorrectly setup";
            ConfigurationException exception = new ConfigurationException(message, cause);
            LOG.error(message, exception);
            throw exception;
        }

        return true;
    }

    private boolean propertyIsSetButDependentIsNotSet(Object object) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {

        boolean propertyNotNullOrEmpty = BeanUtils.getProperty(object, propertyName) != null &&
                !BeanUtils.getProperty(object, propertyName).trim().isEmpty();
        boolean dependentPropertyNullOrEmpty = BeanUtils.getProperty(object, dependentPropertyName) == null ||
                BeanUtils.getProperty(object, dependentPropertyName).trim().isEmpty();

        return propertyNotNullOrEmpty && dependentPropertyNullOrEmpty;
    }
}
