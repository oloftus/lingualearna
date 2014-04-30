package com.lingualearna.web.translation.provider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.util.ApplicationException;
import com.lingualearna.web.util.locale.LocalizationService;

@Service
@Qualifier("GoogleTranslate")
public class GoogleTranslationProvider implements TranslationProvider {

    private static final String BLANK = "";
    private static final Logger LOG = LoggerFactory.getLogger(GoogleTranslationProvider.class);
    private static final String API_URL_REGEX = "(^https?://[^/]*)(/.*$)";

    @Value("${translation.service.google.appName}")
    private String applicationName;

    @Value("${translation.service.google.apiKey}")
    private String apiKey;

    @Value("${translation.service.google.apiUrl}")
    private String apiUrl;

    @Autowired
    private LocalizationService localizationService;

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
    public SingleTranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException, ApplicationException {

        String result;
        if (query.trim().equals(BLANK)) {
            result = BLANK;
        }
        else {
            try {
                JsonFactory jsonFactory = googleTranslateLibraryWrapper.getJsonFactory();
                HttpTransport httpTransport = googleTranslateLibraryWrapper.getHttpTransport();

                Translate client = googleTranslateLibraryWrapper.getTranslateBuilder(httpTransport, jsonFactory, null)
                        .setGoogleClientRequestInitializer(new TranslateRequestInitializer(apiKey))
                        .setApplicationName(applicationName)
                        .setRootUrl(apiUrlRootUrl)
                        .setServicePath(apiUrlServicePath)
                        .build();
                List translateList = googleTranslateLibraryWrapper.setupClient(client, targetLang.getLanguage(), Lists
                        .newArrayList(query));
                googleTranslateLibraryWrapper.setSourceLang(translateList, sourceLang.getLanguage());

                try {
                    result = googleTranslateLibraryWrapper.executeAndGetTranslation(translateList);
                }
                catch (WrappedGoogleJsonResponseException e) {
                    if (e.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                        String localizedMessage = localizationService
                                .lookupLocalizedString("translation.languageUnsupported");
                        throw new TranslationException(localizedMessage, e);
                    }
                    else {
                        throw e.getRootGoogleJsonResponseException();
                    }
                }
            }
            catch (IOException | GeneralSecurityException e) {
                String localizedMessage = localizationService.lookupLocalizedString("translation.genericProblem");
                LOG.error("Exception getting HTTPTransport object in GoogleTranslationProvider", e);
                throw new ApplicationException(localizedMessage, e);
            }
        }

        return new SingleTranslationResult(result);
    }
}
