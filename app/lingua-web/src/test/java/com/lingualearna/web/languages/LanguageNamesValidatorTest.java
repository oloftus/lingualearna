package com.lingualearna.web.languages;

import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.service.LanguageService;
import com.lingualearna.web.translation.SupportedLanguage;

@RunWith(MockitoJUnitRunner.class)
public class LanguageNamesValidatorTest {

    private static final Locale SUPPORTED_LANGUAGE = Locale.forLanguageTag("en");
    private static final Locale UNSUPPORTED_LANGUAGE = Locale.forLanguageTag("fr");

    @Mock
    private LanguageService languageService;

    @Mock
    private SupportedLanguage supportedLanguage;

    @InjectMocks
    private LanguageNamesValidator validator = new LanguageNamesValidator();

    @Before
    public void setup() {

        when(supportedLanguage.getLangCode()).thenReturn(SUPPORTED_LANGUAGE);
        when(languageService.getAllSupportedLanguages()).thenReturn(Lists.newArrayList(supportedLanguage));
    }

    @Test
    public void testValidateLanguageNamesAcceptsSupportedLanguages() throws ValidationException {

        validator.validateLanguageNames(SUPPORTED_LANGUAGE.getLanguage());
    }

    @Test(expected = ValidationException.class)
    public void testValidateLanguageNamesRejectsUnsupportedLanguages() throws ValidationException {

        validator.validateLanguageNames(UNSUPPORTED_LANGUAGE.getLanguage());
    }
}
