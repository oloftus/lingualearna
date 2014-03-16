package com.lingualearna.web.controller.json;

import java.io.Serializable;

public class TranslationRequest implements Serializable {

	private static final long serialVersionUID = 5794467755285069294L;

	private String sourceLang;
	private String targetLang;
	private String query;

	public String getSourceLang() {

		return sourceLang;
	}

	public void setSourceLang(String sourceLang) {

		this.sourceLang = sourceLang;
	}

	public String getTargetLang() {

		return targetLang;
	}

	public void setTargetLang(String targetLang) {

		this.targetLang = targetLang;
	}

	public String getQuery() {

		return query;
	}

	public void setQuery(String query) {

		this.query = query;
	}
}
