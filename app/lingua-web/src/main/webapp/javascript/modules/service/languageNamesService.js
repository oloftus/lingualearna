App.Service.createNew(function() {

    this.isCalled("languageNamesService");

    this.loads("service/jsonWebService");

    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("languageNamesServiceUrl");

    this.hasDefinition(function() {

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
