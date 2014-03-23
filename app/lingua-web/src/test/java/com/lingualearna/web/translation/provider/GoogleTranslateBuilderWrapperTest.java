package com.lingualearna.web.translation.provider;

import static org.mockito.Mockito.verify;

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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.lingualearna.web.testutil.UnitTestBase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Translate.Builder.class })
@PowerMockIgnore("com.google.api.client.http.*")
public class GoogleTranslateBuilderWrapperTest extends UnitTestBase {

	private static final String APPLICATION_NAME = "applicationName";
	private static final String API_URL_ROOT_URL = "apiUrlRootUrl";
	private static final String SERVICE_PATH = "servicePath";
	private static final String BUILDER_FIELD_NAME = "builder";

	private Translate.Builder builder;

	@Mock
	private GoogleClientRequestInitializer googleClientRequestInitializer;

	private GoogleTranslateBuilderWrapper googleTranslateBuilderWrapper;

	@Before
	public void setup() throws Exception {

		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		googleTranslateBuilderWrapper = new GoogleTranslateBuilderWrapper(httpTransport, jsonFactory, null);
		builder = PowerMockito.mock(Translate.Builder.class);
		ReflectionTestUtils.setField(googleTranslateBuilderWrapper, BUILDER_FIELD_NAME, builder);
	}

	@Test
	public void testBuilderDelegatesMethods() {

		whenICallTheBuilderMethods();
		thenTheCallsPassThroughToTheBuilder();
	}

	private void thenTheCallsPassThroughToTheBuilder() {

		verify(builder).setApplicationName(APPLICATION_NAME);
		verify(builder).setGoogleClientRequestInitializer(googleClientRequestInitializer);
		verify(builder).setRootUrl(API_URL_ROOT_URL);
		verify(builder).setServicePath(SERVICE_PATH);
	}

	private void whenICallTheBuilderMethods() {

		googleTranslateBuilderWrapper.setApplicationName(APPLICATION_NAME);
		googleTranslateBuilderWrapper.setGoogleClientRequestInitializer(googleClientRequestInitializer);
		googleTranslateBuilderWrapper.setRootUrl(API_URL_ROOT_URL);
		googleTranslateBuilderWrapper.setServicePath(SERVICE_PATH);
	}
}
