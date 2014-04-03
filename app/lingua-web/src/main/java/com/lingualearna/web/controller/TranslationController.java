package com.lingualearna.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.model.TranslationRequest;
import com.lingualearna.web.controller.model.TranslationResponse;
import com.lingualearna.web.service.TranslationService;
import com.lingualearna.web.translation.TranslationException;
import com.lingualearna.web.translation.TranslationProviderName;
import com.lingualearna.web.util.ApplicationException;

@Controller
@RequestMapping("/api")
public class TranslationController {

	@Autowired
	private TranslationService translationService;

	@RequestMapping(value = "/translate", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public TranslationResponse translateString(@RequestBody TranslationRequest request) throws TranslationException,
			ApplicationException {

		TranslationResponse response = new TranslationResponse();
		Locale targetLangLocale = Locale.forLanguageTag(request.getTargetLang());
		Locale sourceLangLocale = Locale.forLanguageTag(request.getSourceLang());
		String query = request.getQuery();
		response.getTranslations().put(
				TranslationProviderName.Google,
				translationService.translateString(TranslationProviderName.Google, sourceLangLocale, targetLangLocale,
						query)
						.getTargetString());
		response.setQuery(query);
		response.setSourceLang(sourceLangLocale.getLanguage());
		response.setTargetLang(targetLangLocale.getLanguage());

		return response;
	}
}
