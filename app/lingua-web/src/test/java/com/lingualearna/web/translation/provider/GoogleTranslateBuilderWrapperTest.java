package com.lingualearna.web.translation.provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Builder;
import com.lingualearna.web.testutil.UnitTestBase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Builder.class, GoogleTranslateBuilderWrapper.class })
@PowerMockIgnore("com.google.api.client.http.*")
public class GoogleTranslateBuilderWrapperTest extends UnitTestBase {

    private static final String APPLICATION_NAME = "applicationName";
    private static final String API_URL_ROOT_URL = "apiUrlRootUrl";
    private static final String SERVICE_PATH = "servicePath";
    private static final String BUILDER_FIELD_NAME = "builder";

    private Builder builder;
    private JacksonFactory jacksonFactory;
    private NetHttpTransport netHttpTransport;
    private HttpRequestInitializer httpRequestInitializer;

    @Mock
    private Translate expectedTranslateResult;
    private Translate actualTranslateResult;

    @Mock
    private GoogleClientRequestInitializer googleClientRequestInitializer;

    private GoogleTranslateBuilderWrapper googleTranslateBuilderWrapper;

    private void andTheBuildResultIsCorrect() {

        assertEquals(expectedTranslateResult, actualTranslateResult);
    }

    private HttpRequestInitializer getMockHttpRequestInitializer() {

        return new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        };
    }

    private void givenTheBuilderIsSetup() {

        builder = PowerMockito.mock(Builder.class);
        when(builder.build()).thenReturn(expectedTranslateResult);
        ReflectionTestUtils.setField(googleTranslateBuilderWrapper, BUILDER_FIELD_NAME, builder);
    }

    private void givenTheBuilderWrapperIsSetup() throws Exception {

        builder = PowerMockito.mock(Builder.class);
        when(builder.build()).thenReturn(expectedTranslateResult);
        PowerMockito.whenNew(Builder.class).withArguments(netHttpTransport, jacksonFactory,
                httpRequestInitializer).thenReturn(
                builder);
    }

    @Override
    @Before
    public void setup() throws Exception {

        jacksonFactory = JacksonFactory.getDefaultInstance();
        netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        httpRequestInitializer = getMockHttpRequestInitializer();
        googleTranslateBuilderWrapper = new GoogleTranslateBuilderWrapper(netHttpTransport, jacksonFactory, null);
    }

    @Test
    public void testBuilderDelegatesMethods() {

        givenTheBuilderIsSetup();
        whenICallTheBuilderMethods();
        thenTheCallsPassThroughToTheBuilder();
        andTheBuildResultIsCorrect();
    }

    @Test
    public void testConstructorSetsUpBuilderCorrectly() throws Exception {

        givenTheBuilderWrapperIsSetup();
        whenIInstantiateANewBuilderWrapper();
        thenTheTranslateBuilderWasSetupCorrectly();
    }

    private void thenTheCallsPassThroughToTheBuilder() {

        verify(builder).setApplicationName(APPLICATION_NAME);
        verify(builder).setGoogleClientRequestInitializer(googleClientRequestInitializer);
        verify(builder).setRootUrl(API_URL_ROOT_URL);
        verify(builder).setServicePath(SERVICE_PATH);
    }

    private void thenTheTranslateBuilderWasSetupCorrectly() {

        // Testing build as would get an NPE if the Translate.Builder was
        // not/incorrectly setup
        googleTranslateBuilderWrapper.build();
        verify(builder).build();
    }

    private void whenICallTheBuilderMethods() {

        googleTranslateBuilderWrapper.setApplicationName(APPLICATION_NAME);
        googleTranslateBuilderWrapper.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        googleTranslateBuilderWrapper.setRootUrl(API_URL_ROOT_URL);
        googleTranslateBuilderWrapper.setServicePath(SERVICE_PATH);
        actualTranslateResult = googleTranslateBuilderWrapper.build();
    }

    private void whenIInstantiateANewBuilderWrapper() {

        googleTranslateBuilderWrapper = new GoogleTranslateBuilderWrapper(netHttpTransport, jacksonFactory,
                httpRequestInitializer);
    }
}
