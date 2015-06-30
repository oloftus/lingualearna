package com.lingualearna.web.shared.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.data.GenericDao;
import com.lingualearna.web.shared.pojos.Positionable;
import com.lingualearna.web.shared.validation.annotations.ListPosition;
import com.lingualearna.web.shared.validation.annotations.ObjectValidator;

@Component
public class ListPositionValidator extends ObjectValidator implements ConstraintValidator<ListPosition, Positionable> {

    private static final Logger LOG = LoggerFactory.getLogger(ListPositionValidator.class);

    private String maxPositionNamedQuery;
    private String parentProperty;
    private String parentParam;

    @Autowired
    private GenericDao dao;

    @Override
    protected Logger getLog() {

        return LOG;
    }

    @Override
    public void initialize(ListPosition constraintAnnotation) {

        maxPositionNamedQuery = constraintAnnotation.namedQuery();
        parentProperty = constraintAnnotation.parentProperty();
        parentParam = constraintAnnotation.parentParam();
    }

    @Override
    public boolean isValid(Positionable positionalable, ConstraintValidatorContext context) {

        Object parentObject = getPropertyValue(positionalable, parentProperty);
        int position = positionalable.getPosition();

        /*
         * It is necessary to +1 to currentMaxPosition as the decrement which
         * allows the reordering would otherwise show it to be one smaller than
         * it really is.
         * 
         * Also has side effect of allowing you to create a note with maxPos+1,
         * i.e. appending. :)
         */
        int currentMaxPosition = dao.doQueryWithParams(maxPositionNamedQuery, Integer.class, Pair.of(parentParam,
                parentObject));
        currentMaxPosition += 1;

        return 0 < position && position <= currentMaxPosition;
    }
}
