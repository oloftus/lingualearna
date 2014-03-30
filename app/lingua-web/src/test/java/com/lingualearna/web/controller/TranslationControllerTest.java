package com.lingualearna.web.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
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

import com.lingualearna.web.controller.json.TranslationRequest;
import com.lingualearna.web.controller.json.TranslationResponse;
import com.lingualearna.web.service.TranslationService;
import com.lingualearna.web.testutil.UnitTestBase;
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.util.ApplicationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.test.xml")
public class TranslationControllerTest extends UnitTestBase {

	private static final String SOURCE_LANG = "source";
	private static final String TARGET_LANG = "target";
	private static final String QUERY = "query";
	private static final String TRANSLATION = "translation";

	private TranslationResponse translationResult;

	@Mock
	private TranslationRequest translationRequest;

	@Mock
	private TranslationService translationService;

	@Mock
	private SingleTranslationResult singleTranslationResult;

	@Autowired
	@InjectMocks
	private TranslationController translationController;

	@Before
	public void setup() throws Exception {

		super.setup();

		doReturn(TRANSLATION).when(singleTranslationResult).getTargetString();
		when(
				translationService.translateString(TranslationProviderName.Google, Locale.forLanguageTag(SOURCE_LANG),
						Locale
								.forLanguageTag(TARGET_LANG), QUERY)).thenReturn(singleTranslationResult);
	}

	@Test
	public void testTranslateStringDelegatesToTranslationService() throws TranslationException, ApplicationException {

		givenIHaveATranslationRequest();
		whenICallTranslateString();
		thenIGetTheCorrectTranslation();
	}

	private void thenIGetTheCorrectTranslation() {

		assertTrue(translationResult.getQuery().equals(QUERY));
		assertTrue(translationResult.getTargetLang().equals(TARGET_LANG));
		assertTrue(translationResult.getSourceLang().equals(SOURCE_LANG));
		assertTrue(translationResult.getTranslations().size() == 1);
		assertTrue(translationResult.getTranslations().get(TranslationProviderName.Google).equals(TRANSLATION));
	}

	private void givenIHaveATranslationRequest() {

		when(translationRequest.getSourceLang()).thenReturn(SOURCE_LANG);
		when(translationRequest.getTargetLang()).thenReturn(TARGET_LANG);
		when(translationRequest.getQuery()).thenReturn(QUERY);
	}

	private void whenICallTranslateString() throws TranslationException, ApplicationException {

		translationResult = translationController.translateString(translationRequest);
	}
}
