(function(){
    
    var dependencies = [ "linguaApp", "util/commonTypes", "service/jsonWebService" ];
    
    define(dependencies, function(linguaApp) {
        
        var TranslateService = function(jsonWebService, translateServiceUrl) {
            
            var translate = function(translationRequest, successCallback, failureCallback) {
                
                jsonWebService.execute(translateServiceUrl, HttpMethod.POST, translationRequest, successCallback,
                        failureCallback);
            };
            
            return {
                translate : translate
            };
        };
        
        linguaApp.service("translateService", [ "jsonWebService", "translateServiceUrl", TranslateService ]);
    });
})();
