define([], function() {

    TranslationRequest = function(sourceLang, targetLang, query) {

        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.query = query;
    };

    LanguageNameRequest = function(langCode) {

        this.langCode = langCode;
    };

    HttpMethod = {
        POST : "POST",
        GET : "GET"
    };
});
