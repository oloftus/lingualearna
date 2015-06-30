package com.lingualearna.web.shared.validation.validators;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lingualearna.web.shared.validation.annotations.FieldsMatch;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;

/**
 * To be used only on properties whose toString() method returns a blank string
 * for values considered blank.
 */
public class FieldsMatchValidator extends ObjectValidator implements ConstraintValidator<FieldsMatch, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(FieldsMatchValidator.class);
    private static final String BLANK = "";

    private String[] propertyNames;

    @Override
    protected Logger getLog() {

        return LOG;
    }

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {

        propertyNames = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        Set<String> propertyValues = new HashSet<>();

        for (String propertyName : propertyNames) {
            Object propertyValue = getPropertyValue(object, propertyName);
            String propertyValueStr = propertyValue == null ? BLANK : propertyValue.toString();

            propertyValues.add(propertyValueStr);

            if (propertyValues.size() != 1) {
                addViolationPropertyPath(context, propertyName);
                return false;
            }
        }

        return true;
    }
}
