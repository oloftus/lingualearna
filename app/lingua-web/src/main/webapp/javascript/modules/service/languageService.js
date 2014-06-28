App.Service.createNew(function() {

    this.isCalled("languageService");

    this.injects("service/jsonWebService");
    this.injects("util/appCache");

    this.usesConstant("languageNamesServiceUrl");
    this.usesConstant("supportedLanguagesServiceUrl");

    this.hasDefinition(function() {

        var SUPPORED_LANGUAGES_KEY = "languages";

        return function(jsonWebService, appCache, languageNamesServiceUrl, supportedLanguagesServiceUrl) {

            var construct = function() {

                appCache.create(Caches.LANGUAGE_NAMES);
                appCache.create(Caches.SUPPORTED_LANGUAGES);
            };

            var lookupLangName = function(langCode, successCallback, failureCallback) {

                var cachedLanguageName = appCache.lookup(Caches.LANGUAGE_NAMES, langCode);

                if (!_.isUndefined(cachedLanguageName)) {
                    successCallback(cachedLanguageName);
                }
                else {
                    var wrappedSuccessCallback = function(data) {
                        var langName = data.langName;
                        appCache.put(Caches.LANGUAGE_NAMES, langCode, langName);
                        successCallback(langName);
                    };

                    var languageNameRequest = new LanguageNameRequest(langCode);
                    jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest,
                            wrappedSuccessCallback, failureCallback);
                }
            };

            var getSupportedLanguages = function(successCallback, failureCallback) {

                var cachedSupportedLanguages = appCache.lookup(Caches.SUPPORTED_LANGUAGES, SUPPORED_LANGUAGES_KEY);

                if (!_.isUndefined(cachedSupportedLanguages)) {
                    successCallback(cachedSupportedLanguages);
                }
                else {
                    var wrappedSuccessCallback = function(supportedLanguages) {
                        appCache.put(Caches.SUPPORTED_LANGUAGES, SUPPORED_LANGUAGES_KEY, supportedLanguages);
                        successCallback(supportedLanguages);
                    };

                    jsonWebService.execute(supportedLanguagesServiceUrl, HttpMethod.GET, null, wrappedSuccessCallback,
                            failureCallback);
                }
            };

            construct();
            return {
                lookupLangName : lookupLangName,
                getSupportedLanguages : getSupportedLanguages
            };
        };
    });
});
