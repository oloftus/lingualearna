(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "util/commsPipe" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        var JsonWebService = function($http, commsPipe, $state) {

            var callIfNotUndefined = function(callback, thisArg, args) {

                if (!_.isUndefined(callback)) {
                    callback.apply(thisArg, args);
                }
            };
            
            var createCsrfTokenApiUrl = function(apiUrl, csrfSecret) {
                
                return apiUrl + "/" + csrfSecret;
            };

            var getCsrfToken = function(successCallback) {

                execute(createCsrfTokenApiUrl(Properties.csrfTokenApiUrl, Properties.csrfSecret), HttpMethod.GET, null, function(data) {
                    linguaApp.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = data;
                    callIfNotUndefined(successCallback, this);
                });
            };

            var goToLogin = function($state, commsPipe, completedCallback) {

                commsPipe.subscribe(Components.LOGIN, Components.ANY, function(message) {
                    
                    if (message === Signals.LoginSuccess) {
                        getCsrfToken(function() {
                            $state.go(AppStates.MAIN);
                            callIfNotUndefined(completedCallback, this);
                        });
                    }
                });
                
                $state.go(AppStates.LOGIN);
            };

            var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, retryOnAuthentication) {

                var reExecute = function() {

                    execute(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, false);
                };
                
                var successHandler = function(data, status, headers, config) {

                    callIfNotUndefined(successCallback, this, [ data, status, headers, config ]);
                };

                var errorHandler = function(data, status, headers, config) {

                    if (status === HttpHeaders.FORBIDDEN) {
                        if (retryOnAuthentication) {
                            goToLogin($state, commsPipe, reExecute);
                        }
                        else {
                            goToLogin($state, commsPipe);
                        }
                    }
                    else {
                        callIfNotUndefined(failureCallback, this, [ data, status, headers, config ]);
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
