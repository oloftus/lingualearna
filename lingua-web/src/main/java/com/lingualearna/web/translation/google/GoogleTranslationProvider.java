package com.lingualearna.web.translation.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.common.collect.Lists;
import com.lingualearna.web.localisation.LocalisationService;
import com.lingualearna.web.shared.exceptions.UnexpectedProblemException;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationResult;

@Service
@Qualifier("GoogleTranslate")
public class GoogleTranslationProvider implements TranslationProvider {

    private static final String API_URL_REGEX = "(^https?://[^/]*)(/.*$)";
    private static final String BLANK = "";

    @Value("${translation.service.google.appName}")
    private String applicationName;

    @Value("${translation.service.google.apiKey}")
    private String apiKey;

    @Value("${translation.service.google.apiUrl}")
    private String apiUrl;

    @Autowired
    private LocalisationService localizationService;

    @Autowired
    private GoogleTranslateLibraryWrapper googleTranslateLibraryWrapper;

    private String apiUrlRootUrl;
    private String apiUrlServicePath;

    @PostConstruct
    public void init() {

        splitApiUrlComponents();
    }

    private void splitApiUrlComponents() {

        Pattern pattern = Pattern.compile(API_URL_REGEX);
        Matcher matcher = pattern.matcher(apiUrl);
        matcher.matches();
        apiUrlRootUrl = matcher.group(1);
        apiUrlServicePath = matcher.group(2);
    }

    @Override
    public TranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException {

        String result;

        if (query.trim().equals(BLANK)) {
            result = BLANK;
        }
        else {
            try {
                Translate client = buildClient();
                List translateList = setupClientForRequest(sourceLang, targetLang, query, client);
                result = executeRequest(translateList);
            }
            catch (IOException | GeneralSecurityException e) {
                throw new UnexpectedProblemException("Exception setting up Google translation API", e);
            }
        }

        return new TranslationResult(result);
    }

    private String executeRequest(List translateList) throws IOException, TranslationException {

        try {
            return googleTranslateLibraryWrapper.executeAndGetTranslation(translateList);
        }
        catch (WrappedGoogleJsonResponseException e) {
            throw new TranslationException("Exception getting translation from Google translation API", e);
        }
    }

    private List setupClientForRequest(Locale sourceLang, Locale targetLang, String query, Translate client)
            throws IOException {

        List translateList = googleTranslateLibraryWrapper.setupClient(client, targetLang.getLanguage(), Lists
                .newArrayList(query));
        googleTranslateLibraryWrapper.setSourceLang(translateList, sourceLang.getLanguage());

        return translateList;
    }

    private Translate buildClient() throws GeneralSecurityException, IOException {

        JsonFactory jsonFactory = googleTranslateLibraryWrapper.getJsonFactory();
        HttpTransport httpTransport = googleTranslateLibraryWrapper.getHttpTransport();

        Translate client = googleTranslateLibraryWrapper.getTranslateBuilder(httpTransport, jsonFactory, null)
                .setGoogleClientRequestInitializer(new TranslateRequestInitializer(apiKey))
                .setApplicationName(applicationName)
                .setRootUrl(apiUrlRootUrl)
                .setServicePath(apiUrlServicePath)
                .build();

        return client;
    }
}
