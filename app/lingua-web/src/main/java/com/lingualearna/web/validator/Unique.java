package com.lingualearna.web.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.unique}";

    String namedQuery();

    Class<? extends Payload>[] payload() default {};

    String valueParamName();
}
