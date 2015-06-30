define(
    [
        "module/ngModule"
    ],
    function (ngModule) {

        ngModule.factory("abstractController",
            function () {

                return function ($scope) {

                    function setup() {

                        $scope.model = {};
                        $scope.func = {};
                        $scope.model.formMessages = [];
                    }

                    function doExport() {

                        this.setup = setup;
                    }

                    doExport.call(this);
                }
            });
    });
