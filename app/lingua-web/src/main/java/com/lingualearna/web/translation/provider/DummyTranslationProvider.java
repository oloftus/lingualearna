package com.lingualearna.web.translation.provider;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.util.ApplicationException;

@Service
@Qualifier("DummyTranslate")
public class DummyTranslationProvider implements TranslationProvider {

    @Override
    public SingleTranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException, ApplicationException {

        return new SingleTranslationResult("TRANSLATION RESULT");
    }

}
