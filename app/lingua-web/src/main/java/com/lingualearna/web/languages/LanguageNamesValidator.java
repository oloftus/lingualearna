package com.lingualearna.web.languages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.service.LanguageService;
import com.lingualearna.web.translation.SupportedLanguage;

@Component
public class LanguageNamesValidator {

    @Autowired
    private LanguageService languageService;

    /**
     * @throws ValidationException
     *             There is no point adding detail- the invalid state could only
     *             be reached by hacking the API
     */
    public void validateLanguageNames(String... languageCodes) throws ValidationException {

        List<SupportedLanguage> supportedLanguages = languageService.getAllSupportedLanguages();
        Map<String, Boolean> validity = new HashMap<>();

        for (String languageCode : languageCodes) {
            validity.put(languageCode, false);
        }

        for (String languageCode : languageCodes) {
            for (SupportedLanguage supportedLanguage : supportedLanguages) {
                if (languageCode.equals(supportedLanguage.getLangCode())) {
                    validity.put(languageCode, true);
                    break;
                }
            }
        }

        if (validity.containsValue(false)) {
            throw new ValidationException();
        }

        return;
    }
}
