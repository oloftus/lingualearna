package com.lingualearna.web.controller.model;

import java.io.Serializable;

public class LanguageNameRequest implements Serializable {

	private static final long serialVersionUID = 6242660747809580648L;

	private String langCode;

	public String getLangCode() {

		return langCode;
	}

	public void setLangCode(String langCode) {

		this.langCode = langCode;
	}
}
