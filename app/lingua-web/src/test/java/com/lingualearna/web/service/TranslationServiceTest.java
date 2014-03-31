package com.lingualearna.web.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lingualearna.web.testutil.UnitTestBase;
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.util.ApplicationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class TranslationServiceTest extends UnitTestBase {

	private static final String SOURCE_LANG = "source";
	private static final String TARGET_LANG = "target";
	private static final String TRANSLATION = "translation";
	private static final String QUERY = "query";

	@Mock
	private SingleTranslationResult result;

	private Locale sourceLang = Locale.forLanguageTag(SOURCE_LANG);
	private Locale targetLang = Locale.forLanguageTag(TARGET_LANG);

	@Mock
	private TranslationProvider googleTranslationProvider;

	@Autowired
	@InjectMocks
	private TranslationService translationService;

	@Before
	public void setup() throws Exception {

		super.setup();

		when(result.getTargetString()).thenReturn(TRANSLATION);
		when(googleTranslationProvider.translate(sourceLang, targetLang, QUERY)).thenReturn(result);
	}

	@Test
	public void testTranslateStringDelegatesToTranslationProvider() throws TranslationException, ApplicationException {

		whenICallTranslateStringWithGoogleTranslate();
		thenGoogleTranslateProviderIsCalled();
		andIGetTheCorrectTranslation();
	}

	private void andIGetTheCorrectTranslation() {

		assertTrue(result.getTargetString().equals(TRANSLATION));
	}

	private void thenGoogleTranslateProviderIsCalled() throws TranslationException, ApplicationException {

		verify(googleTranslationProvider).translate(sourceLang, targetLang, QUERY);
	}

	private void whenICallTranslateStringWithGoogleTranslate() throws TranslationException, ApplicationException {

		result = translationService.translateString(TranslationProviderName.Google, sourceLang, targetLang, QUERY);
	}
}
