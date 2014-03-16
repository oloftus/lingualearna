package com.lingualearna.web.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.translation.SingleTranslationResult;

@Service
public class TranslationService {

	@Autowired
	@Qualifier("GoogleTranslate")
	private TranslationProvider googleTranslationProvider;

	public SingleTranslationResult translate(TranslationProviderName provider, Locale sourceLang, Locale targetLang,
			String query) throws TranslationException {

		SingleTranslationResult result = null;
		switch (provider) {
			case Google:
				result = googleTranslationProvider.translate(sourceLang, targetLang, query);
		}

		return result;
	}
}
