package com.lingualearna.web.shared.validation.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.language.LanguageService;
import com.lingualearna.web.language.SupportedLanguage;
import com.lingualearna.web.shared.validation.ValidationException;

@Component
public class LanguageNamesValidator {

    @Autowired
    private LanguageService languageService;

    /**
     * @throws 
     *             There is no point adding detail- the invalid state could only
     *             be reached by hacking the API
     */
    public void validateLanguageNames(String... languageCodes) {

        List<SupportedLanguage> supportedLanguages = languageService.getAllSupportedLanguages();
        Map<String, Boolean> validity = new HashMap<>();

        for (String languageCode : languageCodes) {
            validity.put(languageCode, false);
        }

        for (String languageCode : languageCodes) {
            for (SupportedLanguage supportedLanguage : supportedLanguages) {
                if (languageCode.equals(supportedLanguage.getLangCode().getLanguage())) {
                    validity.put(languageCode, true);
                    break;
                }
            }
        }

        if (validity.containsValue(false)) {
            throw new ValidationException();
        }
    }
}
