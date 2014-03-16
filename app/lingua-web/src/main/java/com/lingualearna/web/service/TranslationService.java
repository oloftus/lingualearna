package com.lingualearna.web.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.translation.TranslationResult;

@Service
public class TranslationService {

	@Autowired
	@Qualifier("GoogleTranslate")
	private TranslationProvider googleTranslationProvider;

	public TranslationResult translate(TranslationProviderName provider, Locale sourceLang, Locale targetLang,
			String query) throws TranslationException {

		TranslationResult result = null;
		switch (provider) {
			case Google:
				result = googleTranslationProvider.translate(sourceLang, targetLang, query);
		}

		return result;
	}
}
