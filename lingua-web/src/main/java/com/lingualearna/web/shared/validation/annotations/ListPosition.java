package com.lingualearna.web.shared.validation.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.lingualearna.web.shared.validation.validators.ListPositionValidator;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ListPositionValidator.class)
public @interface ListPosition {

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.listPositionInvalid}";

    Class<? extends Payload>[] payload() default {};

    String namedQuery();

    String parentParam();

    String parentProperty();
}
