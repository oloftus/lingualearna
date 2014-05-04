package com.lingualearna.web.notes;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = MinimumOnePropertyNotEmptyValidator.class)
public @interface MinimumOnePropertyNotEmpty {

    Class<?>[] groups() default {};

    String message() default "{org.lingualearna.web.validationMessages.allNoteFieldsEmpty}";

    Class<? extends Payload>[] payload() default {};

    String[] propertyNames();
}
