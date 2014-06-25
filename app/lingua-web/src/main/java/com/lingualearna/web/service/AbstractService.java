package com.lingualearna.web.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

public abstract class AbstractService {

    protected abstract Validator getValidator();

    protected <T> void validateEntity(T entity) {

        Set<ConstraintViolation<T>> violations = getValidator().validate(entity);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
