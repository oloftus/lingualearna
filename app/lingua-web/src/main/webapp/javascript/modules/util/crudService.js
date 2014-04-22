(function(){
    
    var dependencies = [ "util/commonTypes", "util/jsonWebService" ];
    
    define(dependencies, function() {
        
        /*
         * The intention to use a reference to CrudService later and let Angular to the DI 
         */
        var CrudService = function(jsonWebService, serviceUrl) {
            
            var constructInstanceUrl = function(baseUrl, instanceId) {
                
                return baseUrl + "/" + instanceId;
            };
            
            var create = function(payload, successCallback, failureCallback) {
                jsonWebService.execute(serviceUrl, HttpMethod.POST, payload, successCallback,
                        failureCallback);
                
            };
            
            var retrieve = function(id, successCallback, failureCallback) {
                jsonWebService.execute(constructInstanceUrl(serviceUrl, id), HttpMethod.GET, null, successCallback,
                        failureCallback);
                
            };
            
            var update = function(id, payload, successCallback, failureCallback) {
                jsonWebService.execute(constructInstanceUrl(serviceUrl, id), HttpMethod.PUT, payload, successCallback,
                        failureCallback);
                
            };
            
            var remove = function(id, successCallback, failureCallback) {
                jsonWebService.execute(constructInstanceUrl(serviceUrl, id), HttpMethod.DELETE, null, successCallback,
                        failureCallback);
                
            };
            
            return {
                create : create,
                retrieve : retrieve,
                update : update,
                remove : remove
            };
        };
        
        return CrudService;
    });
})();