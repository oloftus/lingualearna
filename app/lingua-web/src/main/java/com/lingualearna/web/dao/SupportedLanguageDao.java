package com.lingualearna.web.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.service.LanguageService;
import com.lingualearna.web.translation.SupportedLanguage;

@Component
public class SupportedLanguageDao extends AbstractDao {

    @Autowired
    private LanguageService languageService;

    public List<SupportedLanguage> getAllSupportedLanguages() {

        List<SupportedLanguage> results = doQueryAsList(SupportedLanguage.FIND_ALL_QUERY, SupportedLanguage.class);

        for (SupportedLanguage result : results) {
            result.setLanguageService(languageService);
        }

        return results;
    }
}
