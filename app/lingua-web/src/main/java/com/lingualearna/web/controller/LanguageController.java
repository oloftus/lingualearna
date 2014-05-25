package com.lingualearna.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.controller.model.LanguageNameRequest;
import com.lingualearna.web.controller.model.LanguageNameResponse;
import com.lingualearna.web.dao.LanguageDao;
import com.lingualearna.web.languages.SupportedLanguage;
import com.lingualearna.web.service.LanguageService;

@Controller
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageNamesService;

    @Autowired
    private LanguageDao supportedLanguagesDao;

    @RequestMapping(value = "/supported", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<SupportedLanguage> lookupLangName() {

        return supportedLanguagesDao.getAllSupportedLanguages();
    }

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
