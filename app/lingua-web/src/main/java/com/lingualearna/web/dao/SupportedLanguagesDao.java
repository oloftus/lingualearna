package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lingualearna.web.languages.SupportedLanguage;

@Component
public class SupportedLanguagesDao {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDao.class);
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
