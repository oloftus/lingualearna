package com.lingualearna.web.controller.modelmappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BeanUtilsControllerModelMapper<S, T> implements ControllerModelMapper<S, T> {

    @Override
    public void copyPropertiesFromModel(S model, T entity, String... ignore) {

        copyProperties(model, entity, ignore);
    }

    @Override
    public void copyPropertiesFromEntity(T entity, S model, String... ignore) {

        copyProperties(entity, model, ignore);
    }

    private void copyProperties(Object source, Object target, String... ignore) {

        BeanUtils.copyProperties(source, target, ignore);
    }
}
