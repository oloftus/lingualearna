App.Controller.createNew(function() {

    this.isCalled("notebookController");
    
    this.imports("util/appStates");
    this.imports("util/dialogs");

    this.loads("controller/notebookHeaderController");

    this.injects("$scope");
    this.injects("$state");
    this.injects("$timeout");
    this.injects("service/jsonWebService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.extends("controller/abstractRootController");

    this.hasDefinition(function(appStates, dialogs) {
        
        var triggerLogin = function(jsonWebService, commsPipe) {

            jsonWebService.execute(App.Properties.pingServiceUrl, HttpMethod.GET);
            
            var onLoginCallback = function() {
                location.reload();
            };
            commsPipe.subscribe(Components.LOGIN, Components.ANY, onLoginCallback, Signals.LOGIN_SUCCESS);
        };

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            this.setupGlobalScope($scope);
            jsonWebService.setCsrfToken(App.Properties.csrfToken);
            appStates.setMainState(AppStates.NOTEBOOK_MAIN);
            dialogs.setupDialogs($scope);
            this.setupPageMessages($scope, messageHandler, $timeout);
            this.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
            $state.go(AppStates.MAIN).then(function() {
                triggerLogin(jsonWebService, commsPipe);
                this.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            }.bind(this));
        };
    });
});
