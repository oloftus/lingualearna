package com.lingualearna.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.lingualearna.web.languages.SupportedLanguage;

@Component
public class LanguagesDao extends AbstractDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<SupportedLanguage> getAllSupportedLanguages() {

        return doQueryAsList(entityManager, "SupportedLanguage.findAll", SupportedLanguage.class);
    }
}
