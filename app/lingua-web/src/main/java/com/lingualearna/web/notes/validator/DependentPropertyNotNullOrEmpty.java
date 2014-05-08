package com.lingualearna.web.notes.validator;

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
@Constraint(validatedBy = DependentPropertyNotNullOrEmptyValidator.class)
public @interface DependentPropertyNotNullOrEmpty {

    @Target({ java.lang.annotation.ElementType.TYPE })
    @Retention(RUNTIME)
    @interface Properties {

        DependentPropertyNotNullOrEmpty[] value();
    }

    String dependentPropertyName();

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.dependentPropertyNotNull}";

    Class<? extends Payload>[] payload() default {};

    String propertyName();
}
