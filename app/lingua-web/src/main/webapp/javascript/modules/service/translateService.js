App.Service.createNew(function() {

    this.isCalled("translateService");

    this.imports("rootApp");

    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("translateServiceUrl");

    this.hasDefinition(function(rootApp) {

        return function(jsonWebService, translateServiceUrl) {

            var translate = function(translationRequest, successCallback, failureCallback) {

                jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                        failureCallback);
            };

            return {
                translate : translate
            };
        };
    });
});
