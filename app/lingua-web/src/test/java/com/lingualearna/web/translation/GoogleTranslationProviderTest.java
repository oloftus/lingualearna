package com.lingualearna.web.translation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.common.collect.Lists;
import com.lingualearna.web.testutil.AnswerWithSelf;
import com.lingualearna.web.testutil.UnitTestBase;
import com.lingualearna.web.translation.GoogleTranslateBuilderWrapper;
import com.lingualearna.web.translation.GoogleTranslateLibraryWrapper;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.test.xml")
public class GoogleTranslationProviderTest extends UnitTestBase {

	private static final String QUERY = "local";
	private static final String TRANSLATED_STRING = "foreign";
	private static final String SOURCE_LANGUAGE = "en";
	private static final String TARGET_LANGUAGE = "es";
	private static final String APP_NAME = "appName";
	private static final String API_KEY = "apiKey";
	private static final String API_ROOT_URL = "https://example.com";
	private static final String API_SERVICE_PATH = "/api/api";

	@Captor
	private ArgumentCaptor<TranslateRequestInitializer> translateRequestInitializerArg;

	@Captor
	private ArgumentCaptor<ArrayList<String>> arrayListStringArg;

	@Mock
	private GoogleTranslateLibraryWrapper googleTranslateLibraryWrapper;

	@Mock
	private GoogleTranslateBuilderWrapper translatorBuilder;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Translate client;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private List translateList;

	private TranslationResult result;

	@Autowired
	@Qualifier("GoogleTranslate")
	@InjectMocks
	private TranslationProvider translationProvider;

	private void givenTheGoogleTranslateLibraryIsSetup() throws IOException {

		java.util.List<String> query = Lists.newArrayList(QUERY);
		translatorBuilder = mock(GoogleTranslateBuilderWrapper.class, new AnswerWithSelf(
				GoogleTranslateBuilderWrapper.class));
		when(
				googleTranslateLibraryWrapper.getTranslateBuilder(any(HttpTransport.class), any(JsonFactory.class),
						isNull(HttpRequestInitializer.class))).thenReturn(translatorBuilder);
		when(translatorBuilder.build()).thenReturn(client);
		when(client.translations().list(eq(query), eq(TARGET_LANGUAGE))).thenReturn(translateList);
		when(googleTranslateLibraryWrapper.executeAndGetTranslation(translateList)).thenReturn(TRANSLATED_STRING);
	}

	@Test
	public void testTranslateCallsGoogleApi() throws TranslationException, IOException {

		givenTheGoogleTranslateLibraryIsSetup();
		whenICallTranslateThenExceptionIsThrown();
		thenTheCorrectApiMethodsAreCalled();
		andTheTranslationIsCorrect();
	}

	@Test(expected = TranslationException.class)
	public void testTranslateRethrowsAsTranslationException() throws TranslationException, IOException {

		givenTheGoogleTranslateLibraryIsSetup();
		andTheLibraryThrowsAnIOException();
		whenICallTranslateThenExceptionIsThrown();
	}

	private void andTheLibraryThrowsAnIOException() throws IOException {

		when(googleTranslateLibraryWrapper.executeAndGetTranslation(translateList)).thenThrow(new IOException());
	}

	private TranslationResult whenICallTranslateThenExceptionIsThrown() throws TranslationException {

		return whenICallTranslate();
	}

	private void andTheTranslationIsCorrect() {

		assertTrue(result.getTargetString().equals(TRANSLATED_STRING));
	}

	private void thenTheCorrectApiMethodsAreCalled() {

		verify(translatorBuilder).setGoogleClientRequestInitializer(translateRequestInitializerArg.capture());
		assertTrue(translateRequestInitializerArg.getValue().getKey().equals(API_KEY));
		verify(translatorBuilder).setApplicationName(APP_NAME);
		verify(translatorBuilder).setRootUrl(API_ROOT_URL);
		verify(translatorBuilder).setServicePath(API_SERVICE_PATH);
		verify(translateList).setSource(SOURCE_LANGUAGE);
	}

	private TranslationResult whenICallTranslate() throws TranslationException {

		result = translationProvider.translate(Locale.forLanguageTag(SOURCE_LANGUAGE), Locale
				.forLanguageTag(TARGET_LANGUAGE), QUERY);
		return result;
	}
}
