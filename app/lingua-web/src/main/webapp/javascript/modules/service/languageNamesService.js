(function(){
    
    var dependencies = [ "linguaApp", "util/commonTypes", "service/jsonWebService" ];
    
    define(dependencies, function(linguaApp) {
        
        var LanguageNamesService = function(jsonWebService, languageNamesServiceUrl) {
            
            var lookup = function(languageNameRequest, successCallback, failureCallback) {
                jsonWebService.execute(languageNamesServiceUrl, HttpMethod.POST, languageNameRequest, successCallback,
                        failureCallback);
            };
            
            return {
                lookup : lookup
            };
        };
        
        linguaApp.provide.service("languageNamesService", [ "jsonWebService", "languageNamesServiceUrl", LanguageNamesService ]);
    });
})();
