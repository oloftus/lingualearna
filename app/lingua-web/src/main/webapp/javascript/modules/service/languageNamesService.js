App.Service.createNew(function() {

    this.isCalled("languageNamesService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("languageNamesServiceUrl");

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
