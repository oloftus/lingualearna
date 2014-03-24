package com.lingualearna.web.service;

import java.util.Locale;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.util.locale.LocalizationService;

@Service
public class LanguageNamesService {

	private static final String TITLE_FORMAT = "%s (%s)";

	@Autowired
	private LocalizationService localizationService;

	public String lookupLocalizedLangNameAsTitle(String langCode) {

		Locale foreignLocale = Locale.forLanguageTag(langCode);
		Locale localLocale = localizationService.getUserLocale();
		String languageNameForeign = WordUtils.capitalizeFully(lookupLocalizedLangName(langCode));
		String languageNameLocal = WordUtils.capitalizeFully(foreignLocale.getDisplayLanguage(localLocale));

		String title;
		if (!languageNameLocal.equals(languageNameForeign)) {
			title = String.format(TITLE_FORMAT, languageNameForeign, languageNameLocal);
		}
		else {
			title = languageNameForeign;
		}

		return title;
	}

	public String lookupLocalizedLangName(String langCode) {

		Locale foreignLocale = Locale.forLanguageTag(langCode);
		return Locale.forLanguageTag(langCode).getDisplayLanguage(foreignLocale);
	}
}
