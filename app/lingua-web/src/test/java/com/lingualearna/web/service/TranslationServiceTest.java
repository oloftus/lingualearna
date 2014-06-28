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
import org.springframework.beans.factory.annotation.Autowired;

import com.lingualearna.web.exception.ApplicationException;
import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationProviderName;

@RunWith(MockitoJUnitRunner.class)
public class TranslationServiceTest {

    private static final String SOURCE_LANG = "source";
    private static final String TARGET_LANG = "target";
    private static final String TRANSLATION = "translation";
    private static final String QUERY = "query";

    @Mock
    private SingleTranslationResult result;

    @Mock
    private TranslationProvider googleTranslationProvider;

    private Locale sourceLang = Locale.forLanguageTag(SOURCE_LANG);
    private Locale targetLang = Locale.forLanguageTag(TARGET_LANG);

    @Autowired
    @InjectMocks
    private TranslationService translationService;

    private void andIGetTheCorrectTranslation() {

        assertEquals(TRANSLATION, result.getTargetString());
    }

    @Before
    public void setup() throws Exception {

        when(result.getTargetString()).thenReturn(TRANSLATION);
        when(googleTranslationProvider.translate(sourceLang, targetLang, QUERY)).thenReturn(result);
    }

    @Test
    public void testTranslateStringDelegatesToTranslationProvider() throws TranslationException, ApplicationException {

        whenICallTranslateStringWithGoogleTranslate();
        thenGoogleTranslateProviderIsCalled();
        andIGetTheCorrectTranslation();
    }

    private void thenGoogleTranslateProviderIsCalled() throws TranslationException, ApplicationException {

        verify(googleTranslationProvider).translate(sourceLang, targetLang, QUERY);
    }

    private void whenICallTranslateStringWithGoogleTranslate() throws TranslationException, ApplicationException {

        result = translationService.translateString(TranslationProviderName.Google, sourceLang, targetLang, QUERY);
    }
}
