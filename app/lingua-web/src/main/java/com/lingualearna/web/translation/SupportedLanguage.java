package com.lingualearna.web.translation;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lingualearna.web.service.LanguageService;

@NamedQuery(name = SupportedLanguage.FIND_ALL_QUERY, query = "SELECT sl FROM SupportedLanguage sl")
@Entity
@Table(name = "supported_languages")
public class SupportedLanguage implements Serializable {

    private static final long serialVersionUID = -5072464878179191012L;

    public static final String FIND_ALL_QUERY = "SupportedLanguage.findAll";

    @Transient
    private LanguageService languageService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_code")
    private Locale langCode;

    public Locale getLangCode() {

        return langCode;
    }

    public String getLangNameTitle() {

        return languageService.lookupLocalizedLangNameWithTranslation(getLangCode().getLanguage());
    }

    public void setLanguageService(LanguageService languageService) {

        this.languageService = languageService;
    }
}
