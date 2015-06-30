package com.lingualearna.web.shared.objectmappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BeanUtilsObjectMapper<S, T> implements ObjectMapper<S, T> {

    @Override
    public void copyPropertiesLtr(S model, T entity, String... ignore) {

        copyProperties(model, entity, ignore);
    }

    @Override
    public void copyPropertiesRtl(T entity, S model, String... ignore) {

        copyProperties(entity, model, ignore);
    }

    private void copyProperties(Object source, Object target, String... ignore) {

        BeanUtils.copyProperties(source, target, ignore);
    }
}
