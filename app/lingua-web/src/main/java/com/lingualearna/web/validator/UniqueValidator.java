package com.lingualearna.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.dao.GenericDao;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private String namedQuery;
    private String valueParamName;

    @Autowired
    private GenericDao dao;

    @Override
    public void initialize(Unique constraintAnnotation) {

        namedQuery = constraintAnnotation.namedQuery();
        valueParamName = constraintAnnotation.valueParamName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        long count = dao.doUntypedQueryWithParams(namedQuery, Pair.of(valueParamName, value));
        return count == 0;
    }
}
