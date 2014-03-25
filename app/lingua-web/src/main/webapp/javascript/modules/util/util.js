define([ "angular", "appRoot" ], function() {

    function JsonWebService($http){
        
        var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback) {
            $http({
                method : httpMethod,
                url : serviceUrl,
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
            
        return {
            execute : execute
        };
    };

    linguaApp
    
    .service("jsonWebService", [ "$http", JsonWebService ]);
});
