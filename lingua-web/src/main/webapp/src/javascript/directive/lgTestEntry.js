define(
    [
        "module/ngModule",
        "config/properties"
    ],
    function (ngModule, properties) {

        var VIEW_EXTENSION = ".html";

        ngModule.directive("lgTestEntry",
            function () {

                return {
                    restrict: "E", // Match only element names
                    scope: {
                        entry: "=",
                        func: "="
                    },
                    replace: true,
                    templateUrl: properties.templatesRoot + "/directive/lgTestEntry" + VIEW_EXTENSION
                };
            });
    });
