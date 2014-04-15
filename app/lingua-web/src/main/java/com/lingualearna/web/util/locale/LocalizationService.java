package com.lingualearna.web.util.locale;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalizationService {

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
