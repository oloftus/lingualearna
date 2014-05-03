(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var JsonWebService = function($http) {

            var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback) {

                $http({
                    method : httpMethod,
                    url : serviceUrl,
                    withCredentials : true,
                    data : JSON.stringify(requestPayload),
                    headers : {
                        "Content-Type" : "application/json"
                    }
                }).success(function(data, status, headers, config) {
                    successCallback(data, status, headers, config);
                }).error(function(data, status, headers, config) {
                    failureCallback(data, status, headers, config);
                });
            };
            
            var executeSimple = function(serviceUrl, successCallback, failureCallback) {
                
                execute(serviceUrl, HttpMethod.GET, null, successCallback, failureCallback);
            };  

            return {
                execute : execute,
                executeSimple : executeSimple
            };
        };

        ngRegistrationHelper(linguaApp).registerService("jsonWebService", [ "$http", JsonWebService ]);
    });
})();
