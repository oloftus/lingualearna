define([ "util/commonTypes", "util/jsonWebService" ], function() {

    CrudService = function(jsonWebService, serviceUrl) {
        
        var create = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(serviceUrl, HttpMethod.POST, translationRequest, successCallback,
                    failureCallback);

        };
        
        var retrieve = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(serviceUrl, HttpMethod.GET, translationRequest, successCallback,
                    failureCallback);

        };
        
        var update = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(serviceUrl, HttpMethod.PUT, translationRequest, successCallback,
                    failureCallback);

        };
        
        var remove = function(translationRequest, successCallback, failureCallback) {
            jsonWebService.execute(serviceUrl, HttpMethod.DELETE, translationRequest, successCallback,
                    failureCallback);

        };

        return {
            create : create,
            retrieve : retrieve,
            update : update,
            remove : remove
        };
    };
});
