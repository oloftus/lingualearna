package com.lingualearna.web.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.SingleTranslationResult;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProvider;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.util.ApplicationException;

@Service
public class TranslationService {

	@Autowired
	@Qualifier("GoogleTranslate")
	private TranslationProvider googleTranslationProvider;

	public SingleTranslationResult translateString(TranslationProviderName provider, Locale sourceLang, Locale targetLang,
			String query) throws TranslationException, ApplicationException {

		SingleTranslationResult result = null;
		switch (provider) {
			case Google:
				result = googleTranslationProvider.translate(sourceLang, targetLang, query);
		}

		return result;
	}
}
