define(
    [
        "module/ngModule",
        "types/enums",
        "iframe-resizer",
        "state/stateNavigator"
    ],
    function (ngModule, enums) {

        ngModule.controller("settingsController",
            [
                "stateNavigator",

                function (stateNavigator) {

                    iFrameResize({
                        enablePublicMethods: true,
                        checkOrigin: false,
                        heightCalculationMethod: "max",
                        messageCallback: function (message) {

                            if (message.message === enums.Signals.SETTINGS_SAVE_SUCCESS) {
                                stateNavigator.goToParentState();
                            }
                        }
                    });
                }
            ]);
    });
