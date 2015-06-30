package com.lingualearna.web.shared.objectmappers;

public interface ObjectMapper<S, T> {

    public void copyPropertiesRtl(T right, S left, String... ignore);

    public void copyPropertiesLtr(S left, T right, String... ignore);
}
