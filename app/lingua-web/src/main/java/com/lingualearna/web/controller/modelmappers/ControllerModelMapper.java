package com.lingualearna.web.controller.modelmappers;

public interface ControllerModelMapper<S, T> {

    public abstract void copyPropertiesFromModel(S model, T entity, String... ignore);

    public abstract void copyPropertiesFromEntity(T entity, S model, String... ignore);
}