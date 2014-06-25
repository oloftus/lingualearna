package com.lingualearna.web.service;

import java.util.Locale;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.util.locale.LocalizationService;

@Service
public class LanguageService {

    private static final String TITLE_FORMAT = "%s (%s)";

    @Autowired
    private LocalizationService localizationService;

    public String lookupLocalizedLangName(String langCode) {

        Locale foreignLocale = Locale.forLanguageTag(langCode);
        return Locale.forLanguageTag(langCode).getDisplayLanguage(foreignLocale);
    }

    public String lookupLocalizedLangNameWithTranslationAsTitle(String langCode) {

        return lookupLocalizedLangNameWithTranslationWithFormatting(langCode, true);
    }

    public String lookupLocalizedLangNameWithTranslation(String langCode) {

        return lookupLocalizedLangNameWithTranslationWithFormatting(langCode, false);
    }

    private String lookupLocalizedLangNameWithTranslationWithFormatting(String langCode, boolean upperCase) {

        Locale foreignLocale = Locale.forLanguageTag(langCode);
        Locale localLocale = localizationService.getUserLocale();
        String languageNameForeign = lookupLocalizedLangName(langCode);
        String languageNameLocal = foreignLocale.getDisplayLanguage(localLocale);

        if (upperCase) {
            languageNameForeign = WordUtils.capitalizeFully(languageNameForeign);
            languageNameLocal = WordUtils.capitalizeFully(languageNameLocal);
        }

        String title;
        if (!languageNameLocal.equals(languageNameForeign)) {
            title = String.format(TITLE_FORMAT, languageNameForeign, languageNameLocal);
        }
        else {
            title = languageNameForeign;
        }

        return title;

    }
}
