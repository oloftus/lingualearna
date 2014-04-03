package com.lingualearna.web.notes;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependentFieldNoNullValidator implements ConstraintValidator<DependentFieldNotNull, Object> {

	private static final Logger LOG = LoggerFactory.getLogger(DependentFieldNoNullValidator.class);

	private String fieldName;
	private String dependentFieldName;

	@Override
	public void initialize(final DependentFieldNotNull constraintAnnotation) {

		fieldName = constraintAnnotation.fieldName();
		dependentFieldName = constraintAnnotation.dependentFieldName();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {

		try {
			if (BeanUtils.getProperty(object, fieldName) != null
					&& BeanUtils.getProperty(object, dependentFieldName) == null) {
				context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
						.addPropertyNode(dependentFieldName)
						.addConstraintViolation();
				return false;
			}
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOG.error("Error in validator configuration", e);
		}

		return true;
	}
}
