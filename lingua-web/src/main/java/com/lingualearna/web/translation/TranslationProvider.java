package com.lingualearna.web.translation;

import java.util.Locale;

public interface TranslationProvider {

    public TranslationResult translate(Locale sourceLang, Locale targetLang, String query)
            throws TranslationException;
}
