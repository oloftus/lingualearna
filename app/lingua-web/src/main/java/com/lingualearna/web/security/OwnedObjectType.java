package com.lingualearna.web.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in conjuction with the @Secured(ALLOW_OWNER) annotation when object that
 * is owned is specified by ID and not by type.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OwnedObjectType {

    Class<?> value();
}
