package com.lingualearna.web.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lingualearna.web.testutil.UnitTestBase;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.test.xml")
public class LanguageNamesServiceTest extends UnitTestBase {

	private static final String EN_LANG_CODE = "en";
	private static final String EN_LANG_NAME_LOCAL = "English";
	private static final String EN_LANG_NAME_FOREIGN = "English";
	private static final String DE_LANG_CODE = "de";
	private static final String DE_LANG_NAME_LOCAL = "German";
	private static final String DE_LANG_NAME_FOREIGN = "Deutsch";
	private static final String FR_LANG_CODE = "fr";
	private static final String FR_LANG_NAME_LOCAL = "French";
	private static final String FR_LANG_NAME_FOREIGN = "fran\u00e7ais";
	private static final String FR_LANG_NAME_FOREIGN_CAPS = "Fran\u00e7ais";

	@Mock
	private LocalizationService localizationService;

	@Autowired
	private LanguageNamesService languageNamesService;

	@Before
	public void setup() throws Exception {

		super.setup();

		Locale enLocale = Locale.forLanguageTag(EN_LANG_CODE);
		when(localizationService.getUserLocale()).thenReturn(enLocale);
	}

	@Test
	public void testLookupLocalizedLangNameAsTitleReturnsCorrectLanguageName() {

		whenICallLookupLocalizedLangNameAsTitleThenIExpectTheCorrectLanguageNameAndFormat();
	}

	@Test
	public void testlookupLocalizedLangName() {

		whenICallLookupLocalizedLangNameThenIExpectTheCorrectLanguageName();
	}

	private void whenICallLookupLocalizedLangNameThenIExpectTheCorrectLanguageName() {

		assertTrue(languageNamesService.lookupLocalizedLangName(EN_LANG_CODE).equals(EN_LANG_NAME_FOREIGN));
		assertTrue(languageNamesService.lookupLocalizedLangName(DE_LANG_CODE).equals(DE_LANG_NAME_FOREIGN));
		assertTrue(languageNamesService.lookupLocalizedLangName(FR_LANG_CODE).equals(FR_LANG_NAME_FOREIGN));
	}

	private void whenICallLookupLocalizedLangNameAsTitleThenIExpectTheCorrectLanguageNameAndFormat() {

		assertTrue(languageNamesService.lookupLocalizedLangNameAsTitle(EN_LANG_CODE).equals(
				EN_LANG_NAME_FOREIGN + " (" + EN_LANG_NAME_LOCAL + ")"));
		assertTrue(languageNamesService.lookupLocalizedLangNameAsTitle(DE_LANG_CODE).equals(
				DE_LANG_NAME_FOREIGN + " (" + DE_LANG_NAME_LOCAL + ")"));
		assertTrue(languageNamesService.lookupLocalizedLangNameAsTitle(FR_LANG_CODE).equals(
				FR_LANG_NAME_FOREIGN_CAPS + " (" + FR_LANG_NAME_LOCAL + ")"));
	}
}
