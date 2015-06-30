package com.lingualearna.web.language;

import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.localisation.LocalisationService;

@Service
@Transactional
public class LanguageService {

    private static final String TITLE_FORMAT = "%s (%s)";

    @Autowired
    private LocalisationService localizationService;

    @Autowired
    private SupportedLanguageDao supportedLanguagesDao;

    public List<SupportedLanguage> getAllSupportedLanguages() {

        return supportedLanguagesDao.getAllSupportedLanguages();
    }

    public String lookupLocalizedLangName(String langCode) {

        Locale foreignLocale = Locale.forLanguageTag(langCode);
        return Locale.forLanguageTag(langCode).getDisplayLanguage(foreignLocale);
    }

    public String lookupLocalizedLangNameWithTranslation(String langCode) {

        return lookupLocalizedLangNameWithTranslationWithFormatting(langCode, false);
    }

    public String lookupLocalizedLangNameWithTranslationAsTitle(String langCode) {

        return lookupLocalizedLangNameWithTranslationWithFormatting(langCode, true);
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
