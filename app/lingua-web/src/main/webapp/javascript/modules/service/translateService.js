define([ "util/commonTypes" ], function() {
    
    TranslateService = function(jsonWebService, translateServiceUrl) {

        var translate = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                    failureCallback);

        };

        return {
            translate : translate
        };
    };
});
