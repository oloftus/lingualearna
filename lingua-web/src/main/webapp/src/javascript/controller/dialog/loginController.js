define(
    [
        "module/ngModule",
        "types/enums",
        "iframe-resizer",
        "utility/messageBus"
    ],
    function (ngModule, enums) {

        ngModule.controller("loginController",
            [
                "$scope",
                "$state",
                "messageBus",

                function ($scope, $state, messageBus) {

                    iFrameResize({
                        enablePublicMethods: true,
                        checkOrigin: false,
                        heightCalculationMethod: "max",
                        messageCallback: function (message) {

                            if (message.message === enums.Signals.LOGIN_SUCCESS) {
                                messageBus.send(enums.Components.LOGIN, enums.Components.ANY, enums.Signals.LOGIN_SUCCESS);
                            }
                        }
                    });
                }
            ]);
    });
