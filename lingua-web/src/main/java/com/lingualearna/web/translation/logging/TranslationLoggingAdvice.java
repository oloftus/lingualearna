package com.lingualearna.web.translation.logging;

import java.util.Locale;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.translation.TranslationProviderName;

@Aspect
@Component
public class TranslationLoggingAdvice {

    @Autowired
    private TranslationLoggingService loggingService;

    @Before(value = "execution(* com.lingualearna.web.translation.TranslationService.translateString(..)) &&"
            + "args(provider, sourceLang, targetLang, query)",
            argNames = "provider, sourceLang, targetLang, query")
    public void logTranslationRequest(TranslationProviderName provider, Locale sourceLang, Locale targetLang,
            String query) {

        loggingService.logTranslationRequest(provider);
    }
}
