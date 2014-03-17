package com.lingualearna.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.json.LanguageNameRequest;
import com.lingualearna.web.controller.json.LanguageNameResponse;
import com.lingualearna.web.controller.json.TranslationRequest;
import com.lingualearna.web.controller.json.TranslationResult;
import com.lingualearna.web.service.LanguageNamesService;
import com.lingualearna.web.service.TranslationService;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProviderName;

@Controller
@RequestMapping("/translation")
public class TranslationController {

	@Autowired
	private TranslationService translationService;

	@Autowired
	private LanguageNamesService languageNamesService;

	/*
	 * TODO: Handle exception
	 */
	@RequestMapping(value = "/translate", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public TranslationResult translate(@RequestBody TranslationRequest request) throws TranslationException {

		TranslationResult result = new TranslationResult();
		Locale targetLangLocale = Locale.forLanguageTag(request.getTargetLang());
		Locale sourceLangLocale = Locale.forLanguageTag(request.getSourceLang());
		String query = request.getQuery();
		result.getTranslations().put(
				TranslationProviderName.Google,
				translationService.translate(TranslationProviderName.Google, sourceLangLocale, targetLangLocale, query)
						.getTargetString());
		result.setQuery(query);
		result.setSourceLang(sourceLangLocale.getLanguage());
		result.setTargetLang(targetLangLocale.getLanguage());

		return result;
	}

	@RequestMapping(value = "/languageName", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public LanguageNameResponse translate(@RequestBody LanguageNameRequest request) {

		LanguageNameResponse response = new LanguageNameResponse();
		response.setLangCode(request.getLangCode());
		response.setLangName(languageNamesService.lookup(request.getLangCode()));

		return response;
	}
}
