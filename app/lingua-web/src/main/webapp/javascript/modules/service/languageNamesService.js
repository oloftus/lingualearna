App.Service.createNew(function() {

    this.moduleIsCalled("languageNamesService");

    this.imports("linguaApp");
    this.imports("util/ngRegistrationHelper");

    this.importsNg("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("languageNamesServiceUrl");

    this.hasDefinition(function(linguaApp, ngRegistrationHelper) {

        return function(jsonWebService, languageNamesServiceUrl) {

            var lookup = function(languageNameRequest, successCallback, failureCallback) {

                jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                        failureCallback);
            };

            return {
                lookup : lookup
            };
        };
    });
});
