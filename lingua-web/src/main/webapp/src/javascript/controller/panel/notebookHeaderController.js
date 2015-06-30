define(
    [
        "module/ngModule",
        "types/enums",
        "config/properties",
        "service/jsonWebService",
        "controller/abstractController"
    ],
    function(ngModule, enums, properties) {

        ngModule.controller("notebookHeaderController",
            [
                "$scope",
                "jsonWebService",
                "abstractController",

                function ($scope, jsonWebService, AbstractController) {

                    var abstractController = new AbstractController($scope);

                    function setupClickHandlers() {

                        $scope.func.logout = function() {

                            var successHandler = function () {
                                location.reload();
                            };

                            jsonWebService.execute(properties.logoutUrl, enums.HttpMethod.POST, null, successHandler);
                        };
                    }

                    function construct() {

                        abstractController.setup();
                        setupClickHandlers();
                    }

                    construct();
                }
            ]);
    });

