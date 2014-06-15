(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "util/commsPipe" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        var JsonWebService = function($http, commsPipe, $state) {

            var callIfNotUndefinedOrNull = function(callback, thisArg, args) {

                if (!_.isUndefined(callback) && !_.isNull(callback)) {
                    callback.apply(thisArg, args);
                }
            };
            
            var getCsrfTokenApiUrl = function(apiUrl, csrfSecret) {
                
                return Properties.csrfTokenApiUrl + "/" + Properties.csrfSecret;
            };

            var getCsrfToken = function() {

                var successCallback = function(data) {
                    linguaApp.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = data;
                    commsPipe.send(Components.JSON_WEB_SERVICE, Components.ANY, Signals.CsrfRetrieved);
                };

                var csrfTokenApiUrl = getCsrfTokenApiUrl();
                execute(csrfTokenApiUrl, HttpMethod.GET, null, successCallback, null, true);
            };

            var goToLogin = function($state, commsPipe, isGetCsrfRequest, completedCallback) {

                commsPipe.subscribe(Components.LOGIN, Components.ANY, function(message) {
                    if (isGetCsrfRequest) {
                        getCsrfToken();
                    }
                    else {
                        commsPipe.subscribe(Components.JSON_WEB_SERVICE, Components.ANY, function() {
                            callIfNotUndefinedOrNull(completedCallback, this);
                        }, null, Signals.CsrfRetrieved);
                    }
                    
                    $state.go(AppStates.MAIN);
                }, null, Signals.LoginSuccess);
                
                $state.go(AppStates.LOGIN);
            };

            var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, retryOnAuthentication) {

                var reExecute = function() {

                    execute(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, false);
                };
                
                var successHandler = function(data, status, headers, config) {

                    callIfNotUndefinedOrNull(successCallback, this, [ data, status, headers, config ]);
                };

                var errorHandler = function(data, status, headers, config) {

                    if (status === HttpHeaders.FORBIDDEN) {
                        var isGetCsrfRequest = serviceUrl === getCsrfTokenApiUrl();
                        
                        if (retryOnAuthentication) {
                            goToLogin($state, commsPipe, isGetCsrfRequest, reExecute);
                        }
                        else {
                            goToLogin($state, commsPipe, isGetCsrfRequest);
                        }
                    }
                    else {
                        callIfNotUndefinedOrNull(failureCallback, this, [ data, status, headers, config ]);
                    }
                };

                $http({
                    method : httpMethod,
                    url : serviceUrl,
                    withCredentials : true,
                    data : JSON.stringify(requestPayload),
                    headers : {
                        "Content-Type" : "application/json"
                    }
                }).success(successHandler).error(errorHandler);
            };

            return {
                execute : execute,
                getCsrfToken : getCsrfToken
            };
        };

        ngRegistrationHelper(linguaApp).registerService("jsonWebService",
                [ "$http", "commsPipe", "$state", JsonWebService ]);
    });
})();
