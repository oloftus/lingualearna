package com.lingualearna.web.translation.provider.google;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.common.collect.Lists;
import com.lingualearna.web.exception.ApplicationException;
import com.lingualearna.web.testutil.AnswerWithSelf;
import com.lingualearna.web.testutil.MockitoUnitTestBase;
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.provider.google.GoogleTranslateBuilderWrapper;
import com.lingualearna.web.translation.provider.google.GoogleTranslateLibraryWrapper;
import com.lingualearna.web.translation.provider.google.GoogleTranslationProvider;
import com.lingualearna.web.translation.provider.google.WrappedGoogleJsonResponseException;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(MockitoJUnitRunner.class)
public class GoogleTranslationProviderTest extends MockitoUnitTestBase {

    private static final String QUERY = "local";
    private static final String TRANSLATED_STRING = "foreign";
    private static final String SOURCE_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "es";
    private static final String APP_NAME = "appName";
    private static final String API_KEY = "apiKey";
    private static final String API_ROOT_URL = "https://example.com";
    private static final String API_SERVICE_PATH = "/api/api";
    private static final String BLANK = "    ";

    @Captor
    private ArgumentCaptor<TranslateRequestInitializer> translateRequestInitializerArg;

    @Captor
    private ArgumentCaptor<ArrayList<String>> arrayListStringArg;

    @Mock
    private GoogleTranslateLibraryWrapper googleTranslateLibraryWrapper;

    @Mock
    private GoogleTranslateBuilderWrapper translatorBuilder;

    @Mock
    private LocalizationService localizationService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Translate client;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private List translateList;

    private String applicationName = APP_NAME;
    private String apiKey = API_KEY;
    private String apiUrl = API_ROOT_URL + API_SERVICE_PATH;
    private JacksonFactory jacksonFactory;
    private NetHttpTransport netHttpTransport;

    private SingleTranslationResult result;

    @InjectMocks
    private GoogleTranslationProvider translationProvider = new GoogleTranslationProvider();

    private void andTheLibraryThrowsAGoogleJsonResponseExceptionWithStatusBadRequest() throws IOException,
            WrappedGoogleJsonResponseException {

        WrappedGoogleJsonResponseException exception = mock(WrappedGoogleJsonResponseException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        when(googleTranslateLibraryWrapper.executeAndGetTranslation(translateList)).thenThrow(exception);
    }

    private void andTheLibraryThrowsAnIOException() throws Exception {

        when(googleTranslateLibraryWrapper.executeAndGetTranslation(translateList)).thenThrow(new IOException());
    }

    private void andTheTranslationIsCorrect() {

        assertEquals(TRANSLATED_STRING, result.getTargetString());
    }

    private SingleTranslationResult doCallTranslate(String query) throws TranslationException, ApplicationException {

        result = translationProvider.translate(Locale.forLanguageTag(SOURCE_LANGUAGE), Locale
                .forLanguageTag(TARGET_LANGUAGE), query);
        return result;
    }

    private void givenTheGoogleTranslateLibraryIsSetup() throws Exception {

        java.util.List<String> query = Lists.newArrayList(QUERY);
        translatorBuilder = mock(GoogleTranslateBuilderWrapper.class, new AnswerWithSelf(
                GoogleTranslateBuilderWrapper.class));

        jacksonFactory = JacksonFactory.getDefaultInstance();
        netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        when(googleTranslateLibraryWrapper.getJsonFactory()).thenReturn(jacksonFactory);
        when(googleTranslateLibraryWrapper.getHttpTransport()).thenReturn(netHttpTransport);
        when(
                googleTranslateLibraryWrapper.getTranslateBuilder(eq(netHttpTransport), eq(jacksonFactory),
                        isNull(HttpRequestInitializer.class))).thenReturn(translatorBuilder);
        when(translatorBuilder.build()).thenReturn(client);
        when(googleTranslateLibraryWrapper.setupClient(eq(client), eq(TARGET_LANGUAGE), eq(Lists.newArrayList(query))))
                .thenReturn(translateList);
        when(googleTranslateLibraryWrapper.executeAndGetTranslation(translateList)).thenReturn(TRANSLATED_STRING);

        ReflectionTestUtils.setField(translationProvider, "applicationName", applicationName);
        ReflectionTestUtils.setField(translationProvider, "apiKey", apiKey);
        ReflectionTestUtils.setField(translationProvider, "apiUrl", apiUrl);

        translationProvider.init();
    }

    @Test
    public void testTranslateCallsGoogleApi() throws Exception {

        givenTheGoogleTranslateLibraryIsSetup();
        whenICallTranslate();
        thenTheCorrectApiMethodsAreCalled();
        andTheTranslationIsCorrect();
    }

    @Test(expected = ApplicationException.class)
    public void testTranslateRethrowsGenericExceptionsAsApplicationException() throws Exception,
            IOException, ApplicationException {

        givenTheGoogleTranslateLibraryIsSetup();
        andTheLibraryThrowsAnIOException();
        whenICallTranslateThenApplicationExceptionIsThrown();
    }

    @Test(expected = TranslationException.class)
    public void testTranslateRethrowsGoogleJsonResponseExceptionAsTranslateException() throws Exception,
            TranslationException, ApplicationException {

        givenTheGoogleTranslateLibraryIsSetup();
        andTheLibraryThrowsAGoogleJsonResponseExceptionWithStatusBadRequest();
        whenICallTranslateThenTranslateExceptionIsThrown();
    }

    @Test
    public void testTranslateReturnsBlankOnBlankQuery() throws Exception {

        givenTheGoogleTranslateLibraryIsSetup();
        whenICallTranslateWithABlankQuery();
        thenTheResultIsBlank();
    }

    private void thenTheCorrectApiMethodsAreCalled() {

        verify(translatorBuilder).setGoogleClientRequestInitializer(translateRequestInitializerArg.capture());
        assertEquals(API_KEY, translateRequestInitializerArg.getValue().getKey());
        verify(translatorBuilder).setApplicationName(APP_NAME);
        verify(translatorBuilder).setRootUrl(API_ROOT_URL);
        verify(translatorBuilder).setServicePath(API_SERVICE_PATH);
        verify(googleTranslateLibraryWrapper).setSourceLang(translateList, SOURCE_LANGUAGE);
    }

    private void thenTheResultIsBlank() {

        assertEquals(BLANK.trim(), result.getTargetString());
    }

    private SingleTranslationResult whenICallTranslate() throws TranslationException, ApplicationException {

        return doCallTranslate(QUERY);
    }

    private SingleTranslationResult whenICallTranslateThenApplicationExceptionIsThrown() throws TranslationException,
            ApplicationException {

        return whenICallTranslate();
    }

    private void whenICallTranslateThenTranslateExceptionIsThrown() throws TranslationException, ApplicationException {

        whenICallTranslate();
    }

    private SingleTranslationResult whenICallTranslateWithABlankQuery() throws Exception {

        return doCallTranslate(BLANK);
    }
}
