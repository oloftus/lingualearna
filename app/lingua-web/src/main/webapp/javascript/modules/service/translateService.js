define([ "util/commonTypes", "appRoot", "util/util", "util/jsonWebService" ], function() {

    var TranslateService = function(jsonWebService, translateServiceUrl) {

        var translate = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                    failureCallback);

        };

        return {
            translate : translate
        };
    };

    linguaApp.service("translateService", [ "jsonWebService", "translateServiceUrl", TranslateService ]);
});
