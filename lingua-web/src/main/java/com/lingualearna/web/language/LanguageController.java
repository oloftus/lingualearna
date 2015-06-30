package com.lingualearna.web.language;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @RequestMapping(value = "/supported", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<SupportedLanguage> getSupportedLanguages() {

        return languageService.getAllSupportedLanguages();
    }

    @RequestMapping(value = "/langName", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public LanguageNameResponseModel lookupLangName(@RequestBody LanguageNameRequestModel request) {

        String langName = languageService.lookupLocalizedLangNameWithTranslationAsTitle(request.getLangCode());

        LanguageNameResponseModel response = new LanguageNameResponseModel();
        response.setLangCode(request.getLangCode());
        response.setLangName(langName);

        return response;
    }
}
