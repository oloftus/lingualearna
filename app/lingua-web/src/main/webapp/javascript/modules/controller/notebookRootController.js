App.Controller.createNew(function() {

    this.isCalled("notebookRootController");
    
    this.imports("controller/abstractRootController");
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

    this.hasDefinition(function(abstractRootController, appStates, dialogs) {
        
        var triggerLogin = function(jsonWebService, commsPipe) {

            jsonWebService.execute(App.Properties.pingServiceUrl, HttpMethod.GET);
            
            var onLoginCallback = function() {
                location.reload();
            };
            commsPipe.subscribe(Components.LOGIN, Components.ANY, onLoginCallback, Signals.LOGIN_SUCCESS);
        };

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            abstractRootController.setupGlobalScope($scope);
            jsonWebService.setCsrfToken(App.Properties.csrfToken);
            appStates.setMainState(AppStates.NOTEBOOK_MAIN);
            dialogs.setupDialogs($scope);
            abstractRootController.setupPageMessages($scope, messageHandler, $timeout);
            
            $state.go(AppStates.MAIN).then(function() {
                triggerLogin(jsonWebService, commsPipe);
                abstractRootController.setupNotebookEnvironment($scope, $state, notebookService, commsPipe, messageHandler);
                
                var notebookChangedSubscriberId = commsPipe.subscribe(Components.READER, Components.ANY, function() {
                    commsPipe.unsubscribe(notebookChangedSubscriberId);
                    commsPipe.send(Components.NOTEBOOK, Components.ANY, Signals.APP_LOADED);
                }, Signals.CURRENT_NOTEBOOK_CHANGED);
            });
        };
    });
});
