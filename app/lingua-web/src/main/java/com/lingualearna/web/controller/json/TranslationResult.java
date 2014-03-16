package com.lingualearna.web.controller.json;

import java.io.Serializable;
import java.util.Map;

import com.google.api.client.util.Maps;
import com.lingualearna.web.translation.TranslationProviderName;

public class TranslationResult extends TranslationRequest implements Serializable {

	private static final long serialVersionUID = 4684016526009091839L;

	private Map<TranslationProviderName, String> translations;

	public TranslationResult() {

		super();
		this.translations = Maps.newHashMap();
	}

	public Map<TranslationProviderName, String> getTranslations() {

		return translations;
	}

	public void setTranslations(Map<TranslationProviderName, String> translations) {

		this.translations = translations;
	}
}
