package com.lingualearna.web.language;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class SupportedLanguageDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LanguageService languageService;

    public List<SupportedLanguage> getAllSupportedLanguages() {

        List<SupportedLanguage> results = doQueryAsList(SupportedLanguage.FIND_ALL_QUERY, SupportedLanguage.class);

        for (SupportedLanguage result : results) {
            result.setLanguageService(languageService);
        }

        return results;
    }

    @Override
    protected EntityManager getEntityManager() {

        return entityManager;
    }
}
