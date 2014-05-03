package com.lingualearna.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lingualearna.web.controller.model.TranslationRequest;
import com.lingualearna.web.controller.model.TranslationResponse;
import com.lingualearna.web.service.TranslationService;
import com.lingualearna.web.testutil.MockitoUnitTestBase;
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationProviderName;

@RunWith(MockitoJUnitRunner.class)
public class TranslationControllerTest extends MockitoUnitTestBase {

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

    @InjectMocks
    private TranslationController translationController = new TranslationController();

    private void givenIHaveATranslationRequest() {

        when(translationRequest.getSourceLang()).thenReturn(SOURCE_LANG);
        when(translationRequest.getTargetLang()).thenReturn(TARGET_LANG);
        when(translationRequest.getQuery()).thenReturn(QUERY);
    }

    @Override
    @Before
    public void setup() throws Exception {

        doReturn(TRANSLATION).when(singleTranslationResult).getTargetString();
        when(translationService.translateString(TranslationProviderName.Google, Locale.forLanguageTag(SOURCE_LANG),
                Locale.forLanguageTag(TARGET_LANG), QUERY)).thenReturn(singleTranslationResult);
    }

    @Test
    public void testTranslateStringDelegatesToTranslationService() throws Exception {

        givenIHaveATranslationRequest();
        whenICallTranslateString();
        thenIGetTheCorrectTranslation();
    }

    private void thenIGetTheCorrectTranslation() {

        assertEquals(QUERY, translationResult.getQuery());
        assertEquals(TARGET_LANG, translationResult.getTargetLang());
        assertEquals(SOURCE_LANG, translationResult.getSourceLang());
        assertEquals(1, translationResult.getTranslations().size());
        assertEquals(TRANSLATION, translationResult.getTranslations().get(TranslationProviderName.Google));
    }

    private void whenICallTranslateString() throws Exception {

        translationResult = translationController.translateString(translationRequest);
    }
}
