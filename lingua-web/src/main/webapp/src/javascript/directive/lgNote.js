define(
    [
        "module/ngModule",
        "config/properties"
    ],
    function (ngModule, properties) {

        var VIEW_EXTENSION = ".html";

        ngModule.directive("lgNote",
            function () {

                return {
                    restrict: "E", // Match only element names
                    scope: {
                        note: "=",
                        func: "="
                    },
                    replace: true,
                    templateUrl: properties.templatesRoot + "/directive/lgNote" + VIEW_EXTENSION
                };
            });
    });
