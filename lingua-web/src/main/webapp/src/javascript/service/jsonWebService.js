define(
    [
        "module/ngModule",
        "types/enums",
        "config/properties",
        "utility/messageBus",
        "state/stateNavigator"
    ],
    function (ngModule, enums, properties) {

        var CSRF_TOKEN_NAME = "X-CSRF-TOKEN";

        ngModule.service("jsonWebService",
            [
                "$http",
                "$state",
                "messageBus",
                "stateNavigator",

                function ($http, $state, messageBus, stateNavigator) {

                    function callIfNotUndefinedOrNull(callback, thiz, args) {

                        if (!_.isUndefined(callback) && !_.isNull(callback)) {
                            callback.apply(thiz, args);
                        }
                    }

                    function buildCsrfTokenApiUrl() {

                        return properties.csrfTokenApiUrl + "/" + properties.csrfSecret;
                    }

                    function setCsrfToken(csrfToken) {

                        ngModule.httpProvider.defaults.headers.common[CSRF_TOKEN_NAME] = csrfToken;
                    }

                    function goToLogin(isGetCsrfRequest, completedCallback) {

                        function onLoginCallback() {

                            if (isGetCsrfRequest) {
                                acquireCsrfToken();
                            }
                            else {
                                function retryOriginalRequest() {
                                    callIfNotUndefinedOrNull(completedCallback, this);
                                }

                                if (properties.csrfToken) {
                                    retryOriginalRequest();
                                }
                                else {
                                    messageBus.subscribe(enums.Components.JSON_WEB_SERVICE, enums.Components.ANY,
                                        retryOriginalRequest,
                                        enums.Signals.CSRF_RETRIEVED);
                                }
                            }

                            stateNavigator.goToState(enums.AppStates.START);
                        }

                        messageBus.subscribe(enums.Components.LOGIN, enums.Components.ANY, onLoginCallback, enums.Signals.LOGIN_SUCCESS);
                        stateNavigator.goToRelativeState(enums.AppStates.LOGIN);
                    }

                    function acquireCsrfToken() {

                        var csrfTokenApiUrl = buildCsrfTokenApiUrl();

                        function successCallback(data) {
                            setCsrfToken(data);
                            messageBus.send(enums.Components.JSON_WEB_SERVICE, enums.Components.ANY, enums.Signals.CSRF_RETRIEVED);
                        }
                        execute(csrfTokenApiUrl, enums.HttpMethod.GET, null, successCallback, null, true);
                    }

                    function execute(serviceUrl, httpMethod, requestPayload, successCallback, failureCallback, retryOnAuthChallenge) {

                        function successHandler(data, status, headers, config) {

                            callIfNotUndefinedOrNull(successCallback, this, [ data, status, headers, config ]);
                        }

                        function errorHandler(data, status, headers, config) {

                            if (status === enums.HttpHeaders.FORBIDDEN) {
                                var isGetCsrfRequest = serviceUrl === buildCsrfTokenApiUrl();
                                var reExecute = null;
                                if (retryOnAuthChallenge) {
                                    reExecute = function () {
                                        execute(serviceUrl, httpMethod, requestPayload, successCallback,
                                            failureCallback, false);
                                    };
                                }
                                goToLogin(isGetCsrfRequest, reExecute);
                            }
                            else {
                                callIfNotUndefinedOrNull(failureCallback, this, [ data, status, headers, config ]);
                            }
                        }

                        $http({
                            method: httpMethod,
                            url: serviceUrl,
                            withCredentials: true,
                            data: JSON.stringify(requestPayload),
                            headers: {
                                "Content-Type": "application/json"
                            }
                        }).success(successHandler).error(errorHandler);
                    }

                    return {
                        execute: execute,
                        acquireCsrfToken: acquireCsrfToken,
                        setCsrfToken: setCsrfToken
                    };
                }])
    });
