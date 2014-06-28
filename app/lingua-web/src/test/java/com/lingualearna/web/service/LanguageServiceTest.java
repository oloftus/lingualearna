package com.lingualearna.web.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.dao.SupportedLanguageDao;
import com.lingualearna.web.util.locale.LocalizationService;

@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceTest {

    private static final String EN_LANG_CODE = "en";
    private static final String EN_LANG_NAME = "English";
    private static final String DE_LANG_CODE = "de";
    private static final String DE_LANG_NAME_LOCAL = "German";
    private static final String DE_LANG_NAME_FOREIGN = "Deutsch";
    private static final String FR_LANG_CODE = "fr";
    private static final String FR_LANG_NAME_LOCAL = "French";
    private static final String FR_LANG_NAME_FOREIGN = "fran\u00e7ais";
    private static final String FR_LANG_NAME_FOREIGN_CAPS = "Fran\u00e7ais";

    @Mock
    private LocalizationService localizationService;

    @Mock
    private SupportedLanguageDao supportedLanguagesDao;

    @InjectMocks
    private LanguageService languageService = new LanguageService();

    @Before
    public void setup() {

        Locale enLocale = Locale.forLanguageTag(EN_LANG_CODE);
        when(localizationService.getUserLocale()).thenReturn(enLocale);
    }

    @Test
    public void testGetAllSupportedLanguagesDelegatesToDao() {

        whenICallGetAllSupportedLanguages();
        thenTheCallIsDelegatedToTheDao();
    }

    @Test
    public void testlookupLocalizedLangName() {

        whenICallLookupLocalizedLangNameThenIExpectTheCorrectLanguageName();
    }

    @Test
    public void testLookupLocalizedLangNameAsTitleReturnsCorrectLanguageName() {

        whenICallLookupLocalizedLangNameAsTitleThenIExpectTheCorrectLanguageNameAndFormat();
    }

    private void thenTheCallIsDelegatedToTheDao() {

        verify(supportedLanguagesDao).getAllSupportedLanguages();
    }

    private void whenICallGetAllSupportedLanguages() {

        languageService.getAllSupportedLanguages();
    }

    private void whenICallLookupLocalizedLangNameAsTitleThenIExpectTheCorrectLanguageNameAndFormat() {

        assertEquals(EN_LANG_NAME, languageService.lookupLocalizedLangNameWithTranslationAsTitle(EN_LANG_CODE));
        assertEquals(DE_LANG_NAME_FOREIGN + " (" + DE_LANG_NAME_LOCAL + ")",
                languageService.lookupLocalizedLangNameWithTranslationAsTitle(DE_LANG_CODE));
        assertEquals(FR_LANG_NAME_FOREIGN_CAPS + " (" + FR_LANG_NAME_LOCAL + ")",
                languageService.lookupLocalizedLangNameWithTranslationAsTitle(FR_LANG_CODE));
    }

    private void whenICallLookupLocalizedLangNameThenIExpectTheCorrectLanguageName() {

        assertEquals(EN_LANG_NAME, languageService.lookupLocalizedLangName(EN_LANG_CODE));
        assertEquals(DE_LANG_NAME_FOREIGN, languageService.lookupLocalizedLangName(DE_LANG_CODE));
        assertEquals(FR_LANG_NAME_FOREIGN, languageService.lookupLocalizedLangName(FR_LANG_CODE));
    }
}
