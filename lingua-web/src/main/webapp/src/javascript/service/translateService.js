define(
    [
        "module/ngModule",
        "types/enums",
        "service/jsonWebService"
    ],
    function (ngModule, enums) {

        ngModule.service("translateService",
            [
                "jsonWebService",
                "translateServiceUrl",

                function (jsonWebService, translateServiceUrl) {

                    function translate(translationRequest, successCallback, failureCallback) {

                        jsonWebService.execute(translateServiceUrl, enums.HttpMethod.POST, translationRequest,
                            successCallback, failureCallback);
                    }

                    return {
                        translate: translate
                    };
                }
            ]);
    });
