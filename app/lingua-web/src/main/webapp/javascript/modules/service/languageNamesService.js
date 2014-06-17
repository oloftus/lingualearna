(function() {

    var imports = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(imports, function(linguaApp, ngRegistrationHelper) {

        var LanguageNamesService = function(jsonWebService, languageNamesServiceUrl) {

            var lookup = function(languageNameRequest, successCallback, failureCallback) {

                jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                        failureCallback);
            };

            return {
                lookup : lookup
            };
        };

        ngRegistrationHelper(linguaApp).registerService("languageNamesService",
                [ "jsonWebService", "languageNamesServiceUrl", LanguageNamesService ]);
    });
})();
