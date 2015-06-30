package com.lingualearna.web.translation;

import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lingualearna.web.translation.collins.CollinsDictionaryResultTransformer;

@Service
@Transactional
public class TranslationService {

    private static final String BLANK = "";

    @Autowired
    @Qualifier("GoogleTranslate")
    private TranslationProvider googleTranslationProvider;

    @Autowired
    @Qualifier("CollinsDictionary")
    private TranslationProvider collinsTranslationProvider;

    @Autowired
    private CollinsDictionaryResultTransformer collinsDictionaryResultTransformer;

    public TranslationResult translateString(TranslationProviderName provider, Locale sourceLang, Locale targetLang,
            String query) throws TranslationException {

        if (query.trim().isEmpty()) {
            return new TranslationResult(BLANK);
        }

        switch (provider) {
            case Google:
                return googleTranslationProvider.translate(sourceLang, targetLang, query);
            case Collins:
                TranslationResult translate = collinsTranslationProvider.translate(sourceLang, targetLang, query);
                return collinsDictionaryResultTransformer.transform(translate);
        }

        return null;
    }
}
