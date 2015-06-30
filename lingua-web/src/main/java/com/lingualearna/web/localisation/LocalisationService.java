package com.lingualearna.web.localisation;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.localisation.localeresolvers.UserLocaleResolver;

@Service
public class LocalisationService {

    private static final String BUNDLE_NAME = "StringsBundle";

    @Autowired
    private UserLocaleResolver localeResolver;

    public Locale getUserLocale() {

        return localeResolver.getUserLocale();
    }

    public String lookupLocalizedString(String key) {

        ResourceBundle labels = ResourceBundle.getBundle(BUNDLE_NAME, getUserLocale());
        return labels.getString(key);
    }
}
