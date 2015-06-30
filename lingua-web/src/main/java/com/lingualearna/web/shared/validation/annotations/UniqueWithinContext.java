package com.lingualearna.web.shared.validation.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.lingualearna.web.shared.validation.validators.UniqueWithinContextValidator;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueWithinContextValidator.class)
public @interface UniqueWithinContext {

    String contextParam();

    String contextProperty();

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.unique}";

    String namedQuery();

    String ownIdParam();

    String ownIdProperty();

    Class<? extends Payload>[] payload() default {};

    String uniqueParam();

    String uniqueProperty();
}
