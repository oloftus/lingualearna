package com.lingualearna.web.notes;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * http://stackoverflow.com/questions/9284450
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DependentFieldNotNullValidator.class)
public @interface DependentFieldNotNull {

	String message() default "{validationMessages.dependentFieldNotNull}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String fieldName();

	String dependentFieldName();

	@Target({ java.lang.annotation.ElementType.TYPE })
	@Retention(RUNTIME)
	@interface List {

		DependentFieldNotNull[] value();
	}
}
