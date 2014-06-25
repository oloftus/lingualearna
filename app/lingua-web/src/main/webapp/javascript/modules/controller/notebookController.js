App.Controller.createNew(function() {

    this.isCalled("notebookController");
    
    this.imports("util/appStates");

    this.loads("controller/notebookHeaderController");

    this.injects("$scope");
    this.injects("$state");
    this.injects("$timeout");
    this.injects("service/jsonWebService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.extends("controller/abstractRootController");

    this.hasDefinition(function(appStates) {
        
        var triggerLogin = function(jsonWebService) {

            jsonWebService.execute(App.Properties.pingServiceUrl, HttpMethod.GET);
        };

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            this.setupGlobalScope($scope);
            appStates.setMainState(AppStates.NOTEBOOK_MAIN);

            $state.go(AppStates.MAIN).then(function() {
                
                jsonWebService.setCsrfToken(App.Properties.csrfToken);
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
