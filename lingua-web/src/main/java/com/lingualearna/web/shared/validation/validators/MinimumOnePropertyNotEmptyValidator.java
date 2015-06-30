package com.lingualearna.web.shared.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.shared.validation.annotations.MinimumOnePropertyNotEmpty;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;

/**
 * To be used only on properties whose toString() method returns a blank string
 * for values considered blank.
 */
public class MinimumOnePropertyNotEmptyValidator extends ObjectValidator implements
        ConstraintValidator<MinimumOnePropertyNotEmpty, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(MinimumOnePropertyNotEmptyValidator.class);

    private static final String BLANK = "";

    private String[] propertyNames;

    @Override
    protected Logger getLog() {

        return LOG;
    }

    @Override
    public void initialize(MinimumOnePropertyNotEmpty constraintAnnotation) {

        propertyNames = constraintAnnotation.propertyNames();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        for (String propertyName : propertyNames) {
            Object propertyValue = getPropertyValue(object, propertyName);

            if (!(propertyValue == null || propertyValue.toString().trim().equals(BLANK))) {
                return true;
            }
        }

        return false;
    }
}
