package com.lingualearna.web.translation.collins;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingualearna.web.shared.exceptions.UnexpectedProblemException;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationResult;

import fr.idm.sk.publish.api.client.light.SkPublishAPI;
import fr.idm.sk.publish.api.client.light.SkPublishAPIException;

@Service
@Qualifier("CollinsDictionary")
public class CollinsDictionaryProvider extends AbstractSkPublishApiConsumer implements TranslationProvider {

    private static final String ERROR_MESSAGE = "Exception getting translation from Collins Dictionary API";
    private static final String ENTRY_CONTENT = "entryContent";
    private static final String SUGGESTIONS = "suggestions";
    private static final String BLANK = "";
    private static final int NUM_DID_YOU_MEAN_RESULTS = 10;

    @Value("${translation.service.collins.apiKey}")
    private String apiKey;

    @Value("${translation.service.collins.apiUrl}")
    private String apiUrl;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {

        objectMapper = new ObjectMapper();
    }

    @Override
    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    @Override
    protected String getApiKey() {

        return apiKey;
    }

    @Override
    protected String getApiUrl() {

        return apiUrl;
    }

    @Override
    public TranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException {

        SkPublishAPI api = getApi();
        String dictionaryCode = getDictionaryCode(sourceLang, targetLang);

        try {
            return entryOrPossibleEntries(query, api, dictionaryCode);
        }
        catch (SkPublishAPIException skPublishAPIException) {
            throw new TranslationException(ERROR_MESSAGE, skPublishAPIException);
        }
        catch (IOException ioException) {
            throw new UnexpectedProblemException(ERROR_MESSAGE, ioException);
        }
    }

    private TranslationResult entryOrPossibleEntries(String query, SkPublishAPI api, String dictionaryCode)
            throws SkPublishAPIException, IOException {

        TranslationResult entryOrPossibleEntries = bestMatchingEntry(query, api, dictionaryCode);

        if (entryOrPossibleEntries == null) {
            entryOrPossibleEntries = didYouMeanEntries(query, api, dictionaryCode);
        }

        return entryOrPossibleEntries;
    }

    private TranslationResult bestMatchingEntry(String query, SkPublishAPI api, String dictionaryCode)
            throws SkPublishAPIException, IOException {

        String bestMatchJson = getBestMatchJson(query, api, dictionaryCode);

        if (bestMatchJson == null) {
            return null;
        }

        HashMap<String, Object> bestMatch = getJsonAsHashMap(bestMatchJson);
        String targetString = (String) bestMatch.get(ENTRY_CONTENT);

        return new TranslationResult(targetString);
    }

    private String getBestMatchJson(String query, SkPublishAPI api, String dictionaryCode)
            throws SkPublishAPIException, IOException {

        try {
            return api.searchFirst(dictionaryCode, query, FORMAT_HTML);
        }
        catch (SkPublishAPIException skPublishAPIException) {
            if (noResults(skPublishAPIException)) {
                return null;
            }
            else {
                throw skPublishAPIException;
            }
        }
    }

    private TranslationResult didYouMeanEntries(String query, SkPublishAPI api, String dictionaryCode)
            throws SkPublishAPIException, IOException {

        HashMap<String, Object> didYouMeanResults = getJsonAsHashMap(api.didYouMean(dictionaryCode, query,
                NUM_DID_YOU_MEAN_RESULTS));

        TranslationResult translationResult = new TranslationResult(BLANK);
        translationResult.setProviderSpecificResponse(didYouMeanResults.get(SUGGESTIONS));

        return translationResult;
    }
}
