define([ "util/commonTypes" ], function() {
    
    LanguageNamesService = function(jsonWebService, languageNamesServiceUrl) {

        var lookup = function(languageNameRequest, successCallback, failureCallback) {
            jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                    failureCallback)
        };

        return {
            lookup : lookup
        };
    };
});
