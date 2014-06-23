App.Controller.createNew(function() {

    this.isCalled("notebookController");
    
    this.imports("controller/abstractMiniAppController");
    this.imports("util/appStates");
    this.imports("underscore");

    this.loads("controller/notebookHeaderController");

    this.injects("$scope");
    this.injects("$state");
    this.injects("$timeout");
    this.injects("service/jsonWebService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractMiniAppController, appStates, _) {

        var triggerLogin = function(jsonWebService) {

            jsonWebService.execute(Properties.pingServiceUrl, HttpMethod.GET);
        };

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            _.extend(this, abstractMiniAppController);
            this.setupGlobalScope($scope);
            appStates.setMainState(AppStates.NOTEBOOK_MAIN);

            $state.go(AppStates.MAIN).then(function() {
                this.setupPageMessages($scope, messageHandler, $timeout);
                this.setupDialogs($scope);
                this.setupSpecialDialogs($scope);
                this.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
                this.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
                triggerLogin(jsonWebService, $scope);
            }.bind(this));
        };
    });
});
