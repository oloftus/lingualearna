package com.lingualearna.web.shared.validation.annotations;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;

import com.lingualearna.web.shared.exceptions.ConfigurationException;

public abstract class ObjectValidator {

    protected void addViolationPropertyPath(ConstraintValidatorContext context, String propertyName) {

        String messageTemplate = context.getDefaultConstraintMessageTemplate();
        ConstraintViolationBuilder violationBuilder = context
                .buildConstraintViolationWithTemplate(messageTemplate);
        NodeBuilderCustomizableContext nbcc = violationBuilder.addPropertyNode(propertyName);
        nbcc.addConstraintViolation();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getPropertyValue(Object value, String propertyName, Class<T> returnType) {

        try {
            return (T) PropertyUtils.getProperty(value, propertyName);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException cause) {
            String message = String.format("%s validator incorrectly setup.", this.getClass().getName());
            ConfigurationException exception = new ConfigurationException(message, cause);
            getLog().error(message, exception);
            throw exception;
        }
    }

    protected Object getPropertyValue(Object value, String propertyName) {

        return getPropertyValue(value, propertyName, Object.class);
    }

    protected abstract Logger getLog();
}
