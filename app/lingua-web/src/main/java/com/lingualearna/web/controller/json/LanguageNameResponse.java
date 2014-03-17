package com.lingualearna.web.controller.json;

public class LanguageNameResponse extends LanguageNameRequest {

	private static final long serialVersionUID = -9159774442282342239L;

	private String langName;

	public String getLangName() {

		return langName;
	}

	public void setLangName(String langName) {

		this.langName = langName;
	}
}
