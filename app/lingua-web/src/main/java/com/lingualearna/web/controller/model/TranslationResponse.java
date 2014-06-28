package com.lingualearna.web.controller.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.lingualearna.web.translation.TranslationProviderName;

public class TranslationResponse extends TranslationRequest implements Serializable {

    private static final long serialVersionUID = 4684016526009091839L;

    private Map<TranslationProviderName, String> translations;

    public TranslationResponse() {

        super();
        this.translations = new HashMap<>();
    }

    public Map<TranslationProviderName, String> getTranslations() {

        return translations;
    }

    public void setTranslations(Map<TranslationProviderName, String> translations) {

        this.translations = translations;
    }
}
