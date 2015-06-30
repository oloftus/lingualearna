package com.lingualearna.web.translation;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/translate")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @RequestMapping(produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public TranslationResponseModel translateString(@RequestBody @Valid TranslationRequestModel request)
            throws TranslationException {

        Locale sourceLocale = Locale.forLanguageTag(request.getSourceLang());
        Locale targetLocale = Locale.forLanguageTag(request.getTargetLang());
        String query = request.getQuery();

        TranslationResponseModel response = new TranslationResponseModel();
        response.setSourceLang(sourceLocale.getLanguage());
        response.setTargetLang(targetLocale.getLanguage());
        response.setQuery(query);

        for (TranslationProviderName provider : TranslationProviderName.values()) {
            TranslationResult result = translationService.translateString(provider, sourceLocale, targetLocale, query);
            response.addTranslation(provider, result.getTargetString());

            if (result.hasProviderSpecificResponse()) {
                response.addProviderSpecificResponse(provider, result.getProviderSpecificResponse());
            }
        }

        return response;
    }
}
