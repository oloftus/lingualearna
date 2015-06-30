define(
    [
        "module/ngModule",
        "config/properties"
    ],
    function (ngModule, properties) {

        var VIEW_EXTENSION = ".html";

        ngModule.directive("lgPage",
            function () {

                return {
                    restrict: "E",
                    scope: {
                        page: "=",
                        func: "=",
                        global: "="
                    },
                    replace: true,
                    templateUrl: properties.templatesRoot + "/directive/lgPage" + VIEW_EXTENSION
                };
            });
    });
