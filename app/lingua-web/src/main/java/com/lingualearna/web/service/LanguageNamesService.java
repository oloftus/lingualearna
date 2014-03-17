package com.lingualearna.web.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class LanguageNamesService {

	public String lookup(String langCode) {

		return Locale.forLanguageTag(langCode).getDisplayLanguage();
	}
}
