package com.lingualearna.web.languages;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.lingualearna.web.service.LanguageNamesService;

@NamedQueries({
        @NamedQuery(name = "SupportedLanguage.findAll", query = "SELECT sl FROM SupportedLanguage sl")
})
@Entity
@Table(name = "supported_languages")
@Configurable
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SupportedLanguage implements Serializable {

    private static final long serialVersionUID = -5072464878179191012L;

    @Transient
    @Autowired
    private LanguageNamesService languageNamesService;

    @Id
    @Column(name = "language_code")
    private Locale language;

    public String getLangName() {

        return languageNamesService.lookupLocalizedLangName(getLanguage().getLanguage());
    }

    public Locale getLanguage() {

        return language;
    }
}
