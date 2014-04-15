package com.lingualearna.web.translation.provider;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;

/**
 * Wraps the Google Translate library which has final classes/methods to permit
 * mocking.
 */
@Component
public class GoogleTranslateLibraryWrapper {

    public String executeAndGetTranslation(List translateList) throws WrappedGoogleJsonResponseException, IOException {

        String translatedText;
        try {
            translatedText = translateList.execute().getTranslations().get(0).getTranslatedText();
        } catch (GoogleJsonResponseException e) {
            throw new WrappedGoogleJsonResponseException(e);
        }
        return translatedText;
    }

    public HttpTransport getHttpTransport() throws GeneralSecurityException, IOException {

        return GoogleNetHttpTransport.newTrustedTransport();
    }

    public JsonFactory getJsonFactory() {

        return JacksonFactory.getDefaultInstance();
    }

    public GoogleTranslateBuilderWrapper getTranslateBuilder(HttpTransport httpTransport, JsonFactory jsonFactory,
            HttpRequestInitializer httpRequestInitializer) {

        return new GoogleTranslateBuilderWrapper(httpTransport, jsonFactory, httpRequestInitializer);
    }

    public void setSourceLang(List translateList, String sourceLang) {

        translateList.setSource(sourceLang);
    }

    public List setupClient(Translate client, String targetLang, java.util.List<String> query)
            throws IOException {

        return client.translations().list(query, targetLang);
    }
}
