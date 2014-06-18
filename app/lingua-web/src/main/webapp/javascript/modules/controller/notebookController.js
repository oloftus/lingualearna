(function() {

    var componentName = "notebookController";

    var imports = [];
    var ngImports = [];
    var ngDependencies = [];

    imports.push("linguaApp");
    imports.push("controller/abstractMiniAppController");
    imports.push("util/ngRegistrationHelper");
    imports.push("util/appStates");
    imports.push("underscore");

    ngImports.push("controller/notebookHeaderController");
    ngImports.push("service/notebookService");
    ngImports.push("service/jsonWebService");
    ngImports.push("util/commsPipe");
    ngImports.push("util/messageHandler");

    ngDependencies.push("$scope");
    ngDependencies.push("$state");
    ngDependencies.push("$timeout");
    ngDependencies.push("jsonWebService");
    ngDependencies.push("notebookService");
    ngDependencies.push("messageHandler");
    ngDependencies.push("commsPipe");

    define(doImport(imports, ngImports), function(linguaApp, abstractMiniAppController, ngRegistrationHelper, appStates, _) {

        var triggerLogin = function(jsonWebService) {

            jsonWebService.execute(Properties.pingServiceUrl, HttpMethod.GET);
        };

        var NotebookController = function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            var self = this;

            _.extend(this, abstractMiniAppController);
            self.setupGlobalScope($scope);
            appStates.setMainState(AppStates.NOTEBOOK_MAIN);

            $state.go(AppStates.MAIN).then(function() {
                self.setupPageMessages($scope, messageHandler, $timeout);
                self.setupDialogs($scope);
                self.setupSpecialDialogs($scope);
                self.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
                self.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
                triggerLogin(jsonWebService, $scope);
            });
        };

        ngRegistrationHelper(linguaApp).registerController(componentName, ngDependencies, NotebookController);
    });
})();
