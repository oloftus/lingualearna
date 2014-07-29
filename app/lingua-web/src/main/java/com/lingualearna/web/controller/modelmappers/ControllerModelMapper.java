package com.lingualearna.web.controller.modelmappers;

public interface ControllerModelMapper<S, T> {

    public void copyPropertiesFromEntity(T entity, S model, String... ignore);

    public void copyPropertiesFromModel(S model, T entity, String... ignore);
}