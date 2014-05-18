package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.lingualearna.web.languages.SupportedLanguage;

@Component
public class SupportedLanguagesDao {

    private static final String FIND_ALL_QUERY = "SupportedLanguage.findAll";

    @PersistenceContext
    private EntityManager entityManager;

    public List<SupportedLanguage> getAllSupportedLanguages() {

        TypedQuery<SupportedLanguage> query = entityManager.createNamedQuery(FIND_ALL_QUERY,
                SupportedLanguage.class);
        List<SupportedLanguage> results = query.getResultList();
        return results;
    }
}
