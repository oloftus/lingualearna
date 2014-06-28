package com.lingualearna.web.translation;

import java.util.Locale;

import com.lingualearna.web.exception.ApplicationException;

public interface TranslationProvider {

	public SingleTranslationResult translate(Locale sourceLang, Locale targetLang, String query)
			throws TranslationException, ApplicationException;
}
