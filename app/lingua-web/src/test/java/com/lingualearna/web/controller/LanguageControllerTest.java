package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.controller.model.LanguageNameRequest;
import com.lingualearna.web.controller.model.LanguageNameResponse;
import com.lingualearna.web.service.LanguageService;
import com.lingualearna.web.testutil.MockitoUnitTestBase;

@RunWith(MockitoJUnitRunner.class)
public class LanguageControllerTest extends MockitoUnitTestBase {

    private static final String SOURCE_LANG = "source";
    private static final String SOURCE_LANG_NAME = "sourceLangName";

    private LanguageNameResponse languageNameResponse;

    @Mock
    private LanguageNameRequest languageNameRequest;

    @Mock
    private LanguageService languageNamesService;

    @InjectMocks
    private LanguageController languageNamesController = new LanguageController();

    private void givenIHaveALanguageNamesRequest() {

        when(languageNameRequest.getLangCode()).thenReturn(SOURCE_LANG);
    }

    @Override
    @Before
    public void setup() throws Exception {

        when(languageNamesService.lookupLocalizedLangNameAsTitle(SOURCE_LANG)).thenReturn(SOURCE_LANG_NAME);
    }

    @Test
    public void testConvertLangCodeToNameDelegatesToLanguageNamesService() {

        givenIHaveALanguageNamesRequest();
        whenICallLookupLangName();
        thenIGetTheCorrectLanguageName();
    }

    private void thenIGetTheCorrectLanguageName() {

        assertEquals(SOURCE_LANG, languageNameResponse.getLangCode());
        assertEquals(SOURCE_LANG_NAME, languageNameResponse.getLangName());
    }

    private void whenICallLookupLangName() {

        languageNameResponse = languageNamesController.lookupLangName(languageNameRequest);
    }
}