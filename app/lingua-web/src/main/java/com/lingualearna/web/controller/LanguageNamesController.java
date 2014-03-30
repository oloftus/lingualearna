package com.lingualearna.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.json.LanguageNameRequest;
import com.lingualearna.web.controller.json.LanguageNameResponse;
import com.lingualearna.web.service.LanguageNamesService;

@Controller
@RequestMapping("/api")
public class LanguageNamesController {

	@Autowired
	private LanguageNamesService languageNamesService;

	@RequestMapping(value = "/langName", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public LanguageNameResponse lookupLangName(@RequestBody LanguageNameRequest request) {

		String langName = languageNamesService.lookupLocalizedLangNameAsTitle(request.getLangCode());

		LanguageNameResponse response = new LanguageNameResponse();
		response.setLangCode(request.getLangCode());
		response.setLangName(langName);

		return response;
	}
}
