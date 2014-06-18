(function() {

    var componentName = "jsonWebService";

    var imports = [];
    var ngImports = [];
    var ngDependencies = [];

    imports.push("linguaApp");
    imports.push("util/ngRegistrationHelper");
    imports.push("util/appStates");

    ngImports.push("util/commsPipe");

    ngDependencies.push("$http");
    ngDependencies.push("commsPipe");
    ngDependencies.push("$state");

    define(doImport(imports, ngImports), function(linguaApp, ngRegistrationHelper, appStates) {

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
                    commsPipe.send(Components.JSON_WEB_SERVICE, Components.ANY, Signals.CSRF_RETRIEVED);
                };

                var csrfTokenApiUrl = getCsrfTokenApiUrl();
                execute(csrfTokenApiUrl, HttpMethod.GET, null, successCallback, null, true);
            };

            var goToLogin = function($state, commsPipe, isGetCsrfRequest, completedCallback) {

                var onLoginCallback = function() {

                    if (isGetCsrfRequest) {
                        getCsrfToken();
                    }
                    else {
                        var onCsrfRetrievedCallback = function() {
                            callIfNotUndefinedOrNull(completedCallback, this);
                        };

                        commsPipe.subscribe(Components.JSON_WEB_SERVICE, Components.ANY, onCsrfRetrievedCallback,
                                Signals.CSRF_RETRIEVED);
                    }

                    $state.go(AppStates.MAIN);
                };

                commsPipe.subscribe(Components.LOGIN, Components.ANY, onLoginCallback, Signals.LOGIN_SUCCESS);
                appStates.goRelative($state, AppStates.LOGIN);
            };

            var execute = function(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback,
                    retryOnAuthentication) {

                var successHandler = function(data, status, headers, config) {

                    callIfNotUndefinedOrNull(successCallback, this, [ data, status, headers, config ]);
                };

                var errorHandler = function(data, status, headers, config) {

                    if (status === HttpHeaders.FORBIDDEN) {
                        var isGetCsrfRequest = serviceUrl === getCsrfTokenApiUrl();

                        if (retryOnAuthentication) {
                            var reExecute = function() {
                                execute(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, false);
                            };
                            
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

        ngRegistrationHelper(linguaApp).registerService(componentName, ngDependencies, JsonWebService);
    });
})();
