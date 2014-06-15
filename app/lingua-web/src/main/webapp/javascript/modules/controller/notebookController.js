(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "controller/abstractMiniAppController",
            "controller/notebookHeaderController", "service/jsonWebService", "util/messageHandler" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper, abstractMiniAppController) {

        var triggerLogin = function(jsonWebService) {

            jsonWebService.execute(Properties.pingServiceUrl, HttpMethod.GET);
        };

        var NotebookController = function($scope, $state, jsonWebService, $timeout, messageHandler) {

            _.extend(this, abstractMiniAppController);
            this.setMainState(AppStates.NOTEBOOK_MAIN);
            this.setupGlobalScope($scope, $state);
            this.setupPageMessages($scope, messageHandler, $timeout);
            this.setupDialogs($scope);
            this.setupSpecialDialogs($scope);
            
            triggerLogin(jsonWebService, $scope);
            
            $state.go(AppStates.NOTEBOOK_MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController("notebookController",
                [ "$scope", "$state", "jsonWebService", "$timeout", "messageHandler", NotebookController ]);
    });
})();
