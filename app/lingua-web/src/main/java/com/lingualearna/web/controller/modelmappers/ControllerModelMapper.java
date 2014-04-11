package com.lingualearna.web.controller.modelmappers;

public interface ControllerModelMapper<S, T> {

    public abstract void fromModel(S model, T entity, String... ignore);

    public abstract void fromEntity(T entity, S model, String... ignore);
}