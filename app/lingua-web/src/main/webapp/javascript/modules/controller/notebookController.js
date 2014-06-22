App.Controller.createNew(function() {

    this.isCalled("notebookController");
    
    this.imports("rootApp");
    this.imports("controller/abstractMiniAppController");
    this.imports("util/appStates");
    this.imports("underscore");

    this.importsNg("controller/notebookHeaderController");
    this.importsNg("service/jsonWebService");
    this.importsNg("service/notebookService");
    this.importsNg("util/commsPipe");
    this.importsNg("util/messageHandler");

    this.dependsOnNg("$scope");
    this.dependsOnNg("$state");
    this.dependsOnNg("$timeout");
    this.dependsOnNg("jsonWebService");
    this.dependsOnNg("notebookService");
    this.dependsOnNg("messageHandler");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(rootApp, abstractMiniAppController, appStates, _) {

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
