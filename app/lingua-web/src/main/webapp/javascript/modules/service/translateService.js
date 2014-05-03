(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "service/jsonWebService" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var TranslateService = function(jsonWebService, translateServiceUrl) {

            var translate = function(translationRequest, successCallback, failureCallback) {

                jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                        failureCallback);
            };

            return {
                translate : translate
            };
        };

        ngRegistrationHelper(linguaApp).registerService("translateService",
                [ "jsonWebService", "translateServiceUrl", TranslateService ]);
    });
})();
