package com.lingualearna.web.translation.provider.google;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.google.common.collect.Lists;
import com.lingualearna.web.testutil.UnitTestBase;
import com.lingualearna.web.translation.provider.google.GoogleTranslateBuilderWrapper;
import com.lingualearna.web.translation.provider.google.GoogleTranslateLibraryWrapper;
import com.lingualearna.web.translation.provider.google.WrappedGoogleJsonResponseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ List.class, TranslationsListResponse.class, TranslationsResource.class,
        GoogleTranslateLibraryWrapper.class, JacksonFactory.class, GoogleNetHttpTransport.class })
@PowerMockIgnore("com.google.api.client.http.*")
public class GoogleTranslateLibraryWrapperTest extends UnitTestBase {

    private static final String TRANSLATION = "translation";
    private static final String QUERY = "query";
    private static final String LANGUAGE = "targetLang";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private JacksonFactory jacksonFactory;
    private NetHttpTransport httpTransport;
    private HttpRequestInitializer httpRequestInitializer;

    @Mock
    private GoogleTranslateBuilderWrapper expectedGoogleTranslateBuilderWrapper;
    private GoogleTranslateBuilderWrapper actualGoogleTranslateBuilderWrapper;
    @Mock
    private List expectedList;
    private List actualList;
    private GoogleJsonResponseException expectedGoogleJsonResponseException;
    private NetHttpTransport actualNetHttpTransport;
    private NetHttpTransport expectedNetHttpTransport;
    private JacksonFactory actualJacksonFactory;
    private JacksonFactory expectedJacksonFactory;
    private String actualTranslation;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private Translate client;
    @Mock
    private java.util.List<TranslationsResource> listTranslationsResource;
    private List translateList;
    private TranslationsListResponse translationsListResponse;
    private TranslationsResource translationsResource;
    private java.util.List<String> query = Lists.newArrayList(QUERY);

    private GoogleTranslateLibraryWrapper googleTranslateLibraryWrapper;

    private HttpRequestInitializer getMockHttpRequestInitializer() {

        return new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        };
    }

    private void givenAGoogleJsonResponseExceptionIsThrownInExecuteAndGetTranslation() throws Exception {

        expectedGoogleJsonResponseException = mock(GoogleJsonResponseException.class);
        when(translateList.execute()).thenThrow(expectedGoogleJsonResponseException);
    }

    private void givenGoogleNetHttpTransportIsSetup() throws Exception {

        expectedNetHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        PowerMockito.mockStatic(GoogleNetHttpTransport.class);
        PowerMockito.when(GoogleNetHttpTransport.newTrustedTransport()).thenReturn(expectedNetHttpTransport);
    }

    private void givenJacksonFactoryIsSetup() {

        expectedJacksonFactory = JacksonFactory.getDefaultInstance();
        PowerMockito.mockStatic(JacksonFactory.class);
        PowerMockito.when(JacksonFactory.getDefaultInstance()).thenReturn(expectedJacksonFactory);
    }

    private void givenTheGoogleTranslateLibraryWrapperIsSetup() throws Exception {

        jacksonFactory = JacksonFactory.getDefaultInstance();
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        httpRequestInitializer = getMockHttpRequestInitializer();
        PowerMockito.whenNew(GoogleTranslateBuilderWrapper.class).withArguments(httpTransport, jacksonFactory,
                httpRequestInitializer).thenReturn(
                expectedGoogleTranslateBuilderWrapper);
    }

    private void givenTheWrapperIsSetupForExecuteAndGetTranslation() throws Exception {

        when(translateList.execute()).thenReturn(translationsListResponse);
        when(translationsListResponse.getTranslations()).thenReturn(listTranslationsResource);
        when(listTranslationsResource.get(0)).thenReturn(translationsResource);
        when(translationsResource.getTranslatedText()).thenReturn(TRANSLATION);
    }

    private void givenTheWrapperIsSetupForSetupClient() throws IOException {

        when(client.translations().list(query, LANGUAGE)).thenReturn(expectedList);
    }

    @Override
    @Before
    public void setup() throws Exception {

        translateList = PowerMockito.mock(List.class);
        translationsListResponse = PowerMockito.mock(TranslationsListResponse.class);
        translationsResource = PowerMockito.mock(TranslationsResource.class);

        googleTranslateLibraryWrapper = new GoogleTranslateLibraryWrapper();

        super.setup();
    }

    @Test
    public void testExecuteAndGetTranslationCallThrough() throws Exception {

        givenTheWrapperIsSetupForExecuteAndGetTranslation();
        whenICallExecuteAndGetTranslation();
        thenExecuteAndGetTranslationHasCalledThrough();
    }

    @Test
    public void testExecuteAndGetTranslationThrowsWrappedException() throws Exception {

        givenAGoogleJsonResponseExceptionIsThrownInExecuteAndGetTranslation();
        thenIExpectAWrappedGoogleJsonResponseExceptionToBeThrown();
        whenICallExecuteAndGetTranslation();
    }

    @Test
    public void testGetHttpTransportCallsThrough() throws Exception {

        givenGoogleNetHttpTransportIsSetup();
        whenICallNewTrustedTransport();
        thenNewTrustedTransportHasCalledThrough();
    }

    @Test
    public void testGetJsonFactoryCallsThrough() {

        givenJacksonFactoryIsSetup();
        whenICallGetDefaultInstance();
        thenGetDefaultInstanceHasCalledThrough();
    }

    @Test
    public void testGetTranslateBuilderCallsThrough() throws Exception {

        givenTheGoogleTranslateLibraryWrapperIsSetup();
        whenICallGetTranslateBuilder();
        thenTheCorrectlySetupBuilderIsReturned();
    }

    @Test
    public void testSetSourceLangCallsThrough() {

        whenICallSetSourceLang();
        thenSetSourceLangHasCalledThrough();
    }

    @Test
    public void testSetupClientCallsThrough() throws Exception {

        givenTheWrapperIsSetupForSetupClient();
        whenICallSetupClient();
        thenSetupClientHasCalledThrough();
    }

    private void thenExecuteAndGetTranslationHasCalledThrough() {

        assertEquals(TRANSLATION, actualTranslation);
    }

    private void thenGetDefaultInstanceHasCalledThrough() {

        assertEquals(expectedJacksonFactory, actualJacksonFactory);
    }

    private void thenIExpectAWrappedGoogleJsonResponseExceptionToBeThrown() {

        thrown.expect(WrappedGoogleJsonResponseException.class);
        thrown.expectCause(equalTo(expectedGoogleJsonResponseException));
    }

    private void thenNewTrustedTransportHasCalledThrough() {

        assertEquals(expectedNetHttpTransport, actualNetHttpTransport);
    }

    private void thenSetSourceLangHasCalledThrough() {

        verify(translateList).setSource(LANGUAGE);
    }

    private void thenSetupClientHasCalledThrough() {

        assertEquals(actualList, expectedList);
    }

    private void thenTheCorrectlySetupBuilderIsReturned() {

        assertEquals(actualGoogleTranslateBuilderWrapper, expectedGoogleTranslateBuilderWrapper);
    }

    private void whenICallExecuteAndGetTranslation() throws Exception {

        actualTranslation = googleTranslateLibraryWrapper.executeAndGetTranslation(translateList);
    }

    private void whenICallGetDefaultInstance() {

        actualJacksonFactory = JacksonFactory.getDefaultInstance();
    }

    private void whenICallGetTranslateBuilder() {

        actualGoogleTranslateBuilderWrapper = googleTranslateLibraryWrapper
                .getTranslateBuilder(httpTransport, jacksonFactory, httpRequestInitializer);
    }

    public void whenICallNewTrustedTransport() throws Exception {

        actualNetHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    private void whenICallSetSourceLang() {

        googleTranslateLibraryWrapper.setSourceLang(translateList, LANGUAGE);
    }

    private void whenICallSetupClient() throws Exception {

        actualList = googleTranslateLibraryWrapper.setupClient(client, LANGUAGE, query);
    }
}
