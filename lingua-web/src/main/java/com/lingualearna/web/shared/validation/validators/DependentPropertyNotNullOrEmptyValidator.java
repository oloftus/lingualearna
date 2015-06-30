package com.lingualearna.web.shared.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.shared.validation.annotations.DependentPropertyNotNullOrEmpty;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;

/**
 * To be used only on properties whose toString() method returns a blank string
 * for values considered blank.
 */
public class DependentPropertyNotNullOrEmptyValidator extends ObjectValidator implements
        ConstraintValidator<DependentPropertyNotNullOrEmpty, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(DependentPropertyNotNullOrEmptyValidator.class);

    private String propertyName;
    private String dependentPropertyName;

    @Override
    public void initialize(DependentPropertyNotNullOrEmpty constraintAnnotation) {

        propertyName = constraintAnnotation.propertyName();
        dependentPropertyName = constraintAnnotation.dependentPropertyName();
    }

    @Override
    protected Logger getLog() {

        return LOG;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        if (propertyIsSetButDependentIsNotSet(object)) {
            addViolationPropertyPath(context, propertyName);
            return false;
        }

        return true;
    }

    private boolean propertyIsSetButDependentIsNotSet(Object object) {

        Object propertyValue = getPropertyValue(object, propertyName);
        Object dependentProperty = getPropertyValue(object, dependentPropertyName);

        boolean propertyNullOrEmpty = propertyValue == null || propertyValue.toString().trim().isEmpty();
        boolean dependentPropertyNullOrEmpty = dependentProperty == null
                || dependentProperty.toString().trim().isEmpty();

        return !propertyNullOrEmpty && dependentPropertyNullOrEmpty;
    }
}
