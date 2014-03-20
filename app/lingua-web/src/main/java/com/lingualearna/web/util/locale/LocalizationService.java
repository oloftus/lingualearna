package com.lingualearna.web.util.locale;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalizationService {

	private static final String BUNDLE_NAME = "StringsBundle";

	@Autowired
	private SimpleLocaleResolver localeResolver;

	public String lookupLocalizedString(String key) {

		ResourceBundle labels = ResourceBundle.getBundle(BUNDLE_NAME, localeResolver.getUserLocale());
		return labels.getString(key);
	}
}
