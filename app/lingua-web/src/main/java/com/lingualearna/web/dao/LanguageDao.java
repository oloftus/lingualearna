package com.lingualearna.web.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.lingualearna.web.languages.SupportedLanguage;

@Component
public class LanguageDao extends AbstractDao {

    public List<SupportedLanguage> getAllSupportedLanguages() {

        return doQueryAsList("SupportedLanguage.findAll", SupportedLanguage.class);
    }
}
