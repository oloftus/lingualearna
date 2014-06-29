package com.lingualearna.web.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FieldsNotEqualValidator.class)
public @interface FieldsNotEqual {

    @Target({ java.lang.annotation.ElementType.TYPE })
    @Retention(RUNTIME)
    @interface Properties {

        FieldsNotEqual[] value();
    }

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.fieldsNotEqual}";

    Class<? extends Payload>[] payload() default {};

    String[] propertyNames();
}
