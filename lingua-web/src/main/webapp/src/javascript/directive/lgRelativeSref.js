define(
    [
        "module/ngModule",
        "state/stateNavigator"
    ],
    function (ngModule) {

        ngModule.directive("lgRelativeSref",
            [
                "stateNavigator",

                function (stateNavigator) {

                    return {
                        restrict: "A", // Match only attribute names
                        link: function (scope, element, attr) {
                            element.on("click", function () {

                                var stateInstruction = attr.lgRelativeSref;
                                stateNavigator.goToState(stateInstruction);
                            });
                        }
                    };
                }]);
    });
