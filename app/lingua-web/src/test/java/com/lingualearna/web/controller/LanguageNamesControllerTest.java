package com.lingualearna.web.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lingualearna.web.controller.json.LanguageNameRequest;
import com.lingualearna.web.controller.json.LanguageNameResponse;
import com.lingualearna.web.service.LanguageNamesService;
import com.lingualearna.web.testutil.UnitTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class LanguageNamesControllerTest extends UnitTestBase {

	private static final String SOURCE_LANG = "source";
	private static final String SOURCE_LANG_NAME = "sourceLangName";

	private LanguageNameResponse languageNameResponse;

	@Mock
	private LanguageNameRequest languageNameRequest;

	@Mock
	private LanguageNamesService languageNamesService;

	@Autowired
	@InjectMocks
	private LanguageNamesController languageNamesController;

	@Before
	public void setup() throws Exception {

		super.setup();
		when(languageNamesService.lookupLocalizedLangNameAsTitle(SOURCE_LANG)).thenReturn(SOURCE_LANG_NAME);
	}

	@Test
	public void testConvertLangCodeToNameDelegatesToLanguageNamesService() {

		givenIHaveALanguageNamesRequest();
		whenICallLookupLangName();
		thenIGetTheCorrectLanguageName();
	}

	private void thenIGetTheCorrectLanguageName() {

		assertTrue(languageNameResponse.getLangCode().equals(SOURCE_LANG));
		assertTrue(languageNameResponse.getLangName().equals(SOURCE_LANG_NAME));
	}

	private void whenICallLookupLangName() {

		languageNameResponse = languageNamesController.lookupLangName(languageNameRequest);
	}

	private void givenIHaveALanguageNamesRequest() {

		when(languageNameRequest.getLangCode()).thenReturn(SOURCE_LANG);
	}
}
