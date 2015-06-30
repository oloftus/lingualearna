package com.lingualearna.web.translation;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class TranslationRequestModel implements Serializable {

    private static final long serialVersionUID = 5794467755285069294L;

    private String query;
    private String sourceLang;
    private String targetLang;

    @NotNull
    public String getQuery() {

        return query;
    }

    @NotNull
    public String getSourceLang() {

        return sourceLang;
    }

    @NotNull
    public String getTargetLang() {

        return targetLang;
    }

    public void setQuery(String query) {

        this.query = query;
    }

    public void setSourceLang(String sourceLang) {

        this.sourceLang = sourceLang;
    }

    public void setTargetLang(String targetLang) {

        this.targetLang = targetLang;
    }
}
