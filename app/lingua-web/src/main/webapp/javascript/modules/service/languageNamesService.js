define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    var LanguageNamesService = function(jsonWebService, languageNamesServiceUrl) {

        var lookup = function(languageNameRequest, successCallback, failureCallback) {
            jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                    failureCallback);
        };

        return {
            lookup : lookup
        };
    };

    linguaApp.service("languageNamesService", [ "jsonWebService", "languageNamesServiceUrl", LanguageNamesService ]);
});
