package com.lingualearna.web.controller.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper<S, T> {

    public void fromModel(S model, T entity, String... ignore) {

        copyProperties(model, entity, ignore);
    }

    public void fromEntity(T entity, S model, String... ignore) {

        copyProperties(entity, model, ignore);
    }

    private void copyProperties(Object source, Object target, String... ignore) {

        BeanUtils.copyProperties(source, target, ignore);
    }
}
