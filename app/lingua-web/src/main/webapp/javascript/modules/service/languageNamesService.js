(function(){
    
    var dependencies = [ "linguaApp", "util/commonTypes", "util/util", "util/jsonWebService" ];
    
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
        
        linguaApp.service("languageNamesService", [ "jsonWebService", "languageNamesServiceUrl", LanguageNamesService ]);
    });
})();
