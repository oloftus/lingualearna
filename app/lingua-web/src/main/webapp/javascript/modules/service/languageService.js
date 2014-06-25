App.Service.createNew(function() {

    this.isCalled("languageService");

    this.injects("service/jsonWebService");
    
    this.usesConstant("languageNamesServiceUrl");
    this.usesConstant("supportedLanguagesServiceUrl");

    this.hasDefinition(function() {

        return function(jsonWebService, languageNamesServiceUrl, supportedLanguagesServiceUrl) {

            var lookupLangName = function(languageNameRequest, successCallback, failureCallback) {

                jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                        failureCallback);
            };
            
            var getSupportedLanguages = function(successCallback, failureCallback) {
                
                jsonWebService.execute(supportedLanguagesServiceUrl, HttpMethod.GET, null, successCallback,
                        failureCallback);
            };

            return {
                lookupLangName : lookupLangName,
                getSupportedLanguages : getSupportedLanguages
            };
        };
    });
});
