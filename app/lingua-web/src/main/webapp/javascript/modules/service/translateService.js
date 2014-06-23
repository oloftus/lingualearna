App.Service.createNew(function() {

    this.isCalled("translateService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("translateServiceUrl");

    this.hasDefinition(function() {

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
