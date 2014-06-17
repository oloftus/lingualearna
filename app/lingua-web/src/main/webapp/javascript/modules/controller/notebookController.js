(function() {

    var imports = [ "linguaApp", "util/ngRegistrationHelper", "controller/abstractMiniAppController",
            "underscore", "util/commsPipe", "service/notebookService", "controller/notebookHeaderController",
            "service/jsonWebService", "util/messageHandler" ];

    define(imports, function(linguaApp, ngRegistrationHelper, abstractMiniAppController, _) {

        var triggerLogin = function(jsonWebService) {

            jsonWebService.execute(Properties.pingServiceUrl, HttpMethod.GET);
        };

        var NotebookController = function($scope, $state, jsonWebService, $timeout, messageHandler, notebookService,
                commsPipe) {

            _.extend(this, abstractMiniAppController);
            this.setMainState(AppStates.NOTEBOOK_MAIN);
            this.setupGlobalScope($scope, $state);
            this.setupPageMessages($scope, messageHandler, $timeout);
            this.setupDialogs($scope);
            this.setupSpecialDialogs($scope);
            this.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            this.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);

            triggerLogin(jsonWebService, $scope);

            $state.go(AppStates.NOTEBOOK_MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController(
                "notebookController",
                [ "$scope", "$state", "jsonWebService", "$timeout", "messageHandler", "notebookService", "commsPipe",
                        NotebookController ]);
    });
})();
