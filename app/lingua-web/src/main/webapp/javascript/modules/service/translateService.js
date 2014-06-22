App.Service.createNew(function() {

    this.moduleIsCalled("translateService");

    this.imports("linguaApp");
    this.imports("util/ngRegistrationHelper");

    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("translateServiceUrl");

    this.hasDefinition(function(linguaApp, ngRegistrationHelper) {

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
