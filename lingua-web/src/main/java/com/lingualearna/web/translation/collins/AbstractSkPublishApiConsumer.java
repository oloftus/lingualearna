package com.lingualearna.web.translation.collins;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.idm.sk.publish.api.client.light.SkPublishAPI;
import fr.idm.sk.publish.api.client.light.SkPublishAPIException;

@SuppressWarnings("deprecation")
public abstract class AbstractSkPublishApiConsumer {

    protected static final String DICTIONARY_NAME_SEPARATOR = "-";
    protected static final String ERROR_CODE = "errorCode";
    protected static final String FORMAT_HTML = "html";
    protected static final Locale NAMING_LOCALE = Locale.ENGLISH;
    protected static final String NO_RESULTS_ERROR_CODE = "NoResults";

    protected abstract String getApiKey();

    protected abstract String getApiUrl();

    protected abstract ObjectMapper getObjectMapper();

    protected SkPublishAPI getApi() {

        HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        SkPublishAPI api = new SkPublishAPI(getApiUrl(), getApiKey(), httpClient);
        api.setRequestHandler(new SkPublishApiRequestHandler());

        return api;
    }

    protected String getDictionaryCode(Locale sourceLang, Locale targetLang) {

        StringBuilder sb = new StringBuilder();

        sb.append(getDictionaryCodePart(sourceLang));
        sb.append(DICTIONARY_NAME_SEPARATOR);
        sb.append(getDictionaryCodePart(targetLang));

        return sb.toString();
    }

    String getDictionaryCodePart(Locale sourceLang) {

        return sourceLang.getDisplayLanguage(NAMING_LOCALE).toLowerCase();
    }

    @SuppressWarnings("unchecked")
    protected HashMap<String, Object> getJsonAsHashMap(String json) throws IOException {

        return getObjectMapper().readValue(json, HashMap.class);
    }

    protected boolean noResults(SkPublishAPIException skPublishAPIException) throws IOException {

        HashMap<String, Object> response = getJsonAsHashMap(skPublishAPIException.getResponse());
        Object errorCode = response.get(ERROR_CODE);

        return errorCode.equals(NO_RESULTS_ERROR_CODE);
    }
}
