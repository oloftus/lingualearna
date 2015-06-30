define(function() {

    return function($scope) {

        function addCleanupStep(func) {

            $scope.global.cleanupActions.push(func);
        }

        function doCleanup() {

            _.each($scope.global.cleanupActions, function (action) {
                action();
            });
            $scope.global.cleanupActions = [];
        }

        function setup($rootScope) {

            $scope.global.doCleanup = function () {
                doCleanup($scope);
            };

            $rootScope.$on("$stateChangeSuccess", function () {
                $scope.global.doCleanup();
            });
        }

        function doExport() {

            this.addCleanupStep = addCleanupStep;
            this.setup = setup;
        }

        doExport.call(this);
    };
});