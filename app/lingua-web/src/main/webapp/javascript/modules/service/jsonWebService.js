App.Service.createNew(function() {

    this.isCalled("jsonWebService");

    this.imports("framework/rootApp");
    this.imports("util/appStates");

    this.injects("$http");
    this.injects("$state");
    this.injects("util/commsPipe");

    this.hasDefinition(function(rootApp, appStates) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        var callIfNotUndefinedOrNull = function(callback, thiz, args) {
            
            if (!_.isUndefined(callback) && !_.isNull(callback)) {
                callback.apply(thiz, args);
            }
        };
        
        var buildCsrfTokenApiUrl = function(apiUrl, csrfSecret) {
            
            return App.Properties.csrfTokenApiUrl + "/" + App.Properties.csrfSecret;
        };
        
        var setCsrfToken = function(csrfToken) {
            
            rootApp.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = csrfToken;
        };
        
        return function($http, $state, commsPipe) {

            var goToLogin = function($state, commsPipe, isGetCsrfRequest, completedCallback) {
                
                var onLoginCallback = function() {
                    
                    if (isGetCsrfRequest) {
                        acquireCsrfToken();
                    }
                    else {
                        var retryOriginalRequest = function() {
                            callIfNotUndefinedOrNull(completedCallback, this);
                        };
                        
                        if (App.Properties.csrfToken) {
                            retryOriginalRequest();
                        }
                        else {
                            commsPipe.subscribe(Components.JSON_WEB_SERVICE, Components.ANY, retryOriginalRequest,
                                    Signals.CSRF_RETRIEVED);
                        }
                    }
                    
                    $state.go(AppStates.MAIN);
                };
                
                commsPipe.subscribe(Components.LOGIN, Components.ANY, onLoginCallback, Signals.LOGIN_SUCCESS);
                appStates.goRelative($state, AppStates.LOGIN);
            };
            
            var acquireCsrfToken = function() {

                var csrfTokenApiUrl = buildCsrfTokenApiUrl();
                var successCallback = function(data) {
                    setCsrfToken(data);
                    commsPipe.send(Components.JSON_WEB_SERVICE, Components.ANY, Signals.CSRF_RETRIEVED);
                };

                execute(csrfTokenApiUrl, HttpMethod.GET, null, successCallback, null, true);
            };

            var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback,
                    retryOnAuthChallenge) {

                var successHandler = function(data, status, headers, config) {

                    callIfNotUndefinedOrNull(successCallback, this, [ data, status, headers, config ]);
                };

                var errorHandler = function(data, status, headers, config) {

                    if (status === HttpHeaders.FORBIDDEN) {
                        var isGetCsrfRequest = serviceUrl === buildCsrfTokenApiUrl();
                        var reExecute = null;
                        if (retryOnAuthChallenge) {
                            reExecute = function() {
                                execute(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, false);
                            };
                        }
                        goToLogin($state, commsPipe, isGetCsrfRequest, reExecute);
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
                acquireCsrfToken : acquireCsrfToken,
                setCsrfToken : setCsrfToken
            };
        };
    });
});
