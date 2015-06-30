define(
    [
        "module/ngModule",
        "types/enums",
        "types/types",
        "service/jsonWebService",
        "utility/cacheManager"
    ],
    function (ngModule, enums, types) {

        var SUPPORTED_LANGUAGES_KEY = "languages";

        ngModule.service("languageService",
            [
                "jsonWebService",
                "cacheManager",
                "languageNamesServiceUrl",
                "supportedLanguagesServiceUrl",

                function (jsonWebService, cacheManager, languageNamesServiceUrl, supportedLanguagesServiceUrl) {

                    function lookupLangName(langCode, successCallback, failureCallback) {

                        var cachedLanguageName = cacheManager.lookup(enums.Caches.LANGUAGE_NAMES, langCode);

                        if (cachedLanguageName) {
                            successCallback(cachedLanguageName);
                            return;
                        }

                        function wrappedSuccessCallback(data) {
                            var langName = data.langName;
                            cacheManager.put(enums.Caches.LANGUAGE_NAMES, langCode, langName);
                            successCallback(langName);
                        }

                        var languageNameRequest = new types.LanguageNameRequest(langCode);
                        jsonWebService.execute(languageNamesServiceUrl, enums.HttpMethod.POST, languageNameRequest,
                            wrappedSuccessCallback, failureCallback);
                    }

                    function getSupportedLanguages(successCallback, failureCallback) {

                        var cachedSupportedLanguages = cacheManager.lookup(enums.Caches.SUPPORTED_LANGUAGES,
                            SUPPORTED_LANGUAGES_KEY);

                        if (cachedSupportedLanguages) {
                            successCallback(cachedSupportedLanguages);
                            return;
                        }

                        function cacheResultAndCallSuccessCallback(supportedLanguages) {

                            cacheManager.put(enums.Caches.SUPPORTED_LANGUAGES, SUPPORTED_LANGUAGES_KEY,
                                supportedLanguages);
                            successCallback(supportedLanguages);
                        }

                        jsonWebService.execute(supportedLanguagesServiceUrl, enums.HttpMethod.GET, null,
                            cacheResultAndCallSuccessCallback, failureCallback);
                    }

                    function construct() {

                        cacheManager.create(enums.Caches.LANGUAGE_NAMES);
                        cacheManager.create(enums.Caches.SUPPORTED_LANGUAGES);
                    }

                    construct();

                    return {
                        lookupLangName: lookupLangName,
                        getSupportedLanguages: getSupportedLanguages
                    };
                }
            ]);
    });