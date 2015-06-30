package com.lingualearna.web.language;

public class LanguageNameResponseModel extends LanguageNameRequestModel {

	private static final long serialVersionUID = -9159774442282342239L;

	private String langName;

	public String getLangName() {

		return langName;
	}

	public void setLangName(String langName) {

		this.langName = langName;
	}
}
