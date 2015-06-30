package com.lingualearna.web.shared.validation.validators;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.shared.validation.annotations.FieldsNotEqual;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;

/**
 * To be used only on properties whose toString() method returns a blank string
 * for values considered blank.
 * 
 * Does not consider blank values to be equal to one another.
 */
public class FieldsNotEqualValidator extends ObjectValidator implements ConstraintValidator<FieldsNotEqual, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(FieldsNotEqualValidator.class);
    private static final String BLANK = "";

    private String[] propertyNames;

    @Override
    public void initialize(FieldsNotEqual constraintAnnotation) {

        propertyNames = constraintAnnotation.value();
    }

    @Override
    protected Logger getLog() {

        return LOG;
    }

    /**
     * Checks by way of elimination by adding to set
     */
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        Set<String> propertyValues = new HashSet<>();
        int numPropertiesChecked = 0;

        for (String propertyName : propertyNames) {
            Object propertyValue = getPropertyValue(object, propertyName);
            String propertyValueStr = propertyValue == null ? BLANK : propertyValue.toString();

            if (propertyValueStr != null && propertyValueStr.trim().length() != 0) {
                propertyValues.add(propertyValueStr);
                numPropertiesChecked++;
            }

            if (propertyValues.size() != numPropertiesChecked) {
                return false;
            }
        }

        return true;
    }
}
