package com.lingualearna.web.translation.dummy;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationResult;

@Service
@Qualifier("DummyTranslate")
public class DummyTranslationProvider implements TranslationProvider {

    @Override
    public TranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException {

        return new TranslationResult("TRANSLATION RESULT");
    }

}
