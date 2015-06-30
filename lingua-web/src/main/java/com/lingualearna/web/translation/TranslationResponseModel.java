package com.lingualearna.web.translation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TranslationResponseModel extends TranslationRequestModel implements Serializable {

    private static final long serialVersionUID = 4684016526009091839L;

    private Map<TranslationProviderName, String> translations;
    private Map<TranslationProviderName, Object> providerSpecificResponses;

    public TranslationResponseModel() {

        super();
        this.translations = new HashMap<>();
        this.providerSpecificResponses = new HashMap<>();
    }

    public void addProviderSpecificResponse(TranslationProviderName translationProviderName,
            Object providerSpecificResponse) {

        getProviderSpecificResponses().put(translationProviderName, providerSpecificResponse);
    }

    public void addTranslation(TranslationProviderName translationProviderName, String translation) {

        getTranslations().put(translationProviderName, translation);
    }

    public Map<TranslationProviderName, Object> getProviderSpecificResponses() {

        return providerSpecificResponses;
    }

    public Map<TranslationProviderName, String> getTranslations() {

        return translations;
    }
}
