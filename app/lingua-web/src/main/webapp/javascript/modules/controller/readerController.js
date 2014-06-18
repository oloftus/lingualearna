(function() {

    var componentName = "readerController";

    var imports = [];
    var ngImports = [];
    var ngDependencies = [];

    imports.push("linguaApp");
    imports.push("controller/abstractMiniAppController");
    imports.push("util/ngRegistrationHelper");
    imports.push("util/textSelector");
    imports.push("util/appStates");
    imports.push("underscore");

    ngImports.push("controller/readerBarController");
    ngImports.push("service/jsonWebService");
    ngImports.push("service/notebookService");
    ngImports.push("util/messageHandler");
    ngImports.push("util/commsPipe");

    ngDependencies.push("$scope");
    ngDependencies.push("$state");
    ngDependencies.push("$timeout");
    ngDependencies.push("jsonWebService");
    ngDependencies.push("notebookService");
    ngDependencies.push("messageHandler");
    ngDependencies.push("commsPipe");

    define(doImport(imports, ngImports), function(linguaApp, abstractMiniAppController, ngRegistrationHelper, textSelector, appStates, _) {

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (!_.isEmpty(selected)) {
                var translationRequest = new TranslationRequest($scope.global.model.currentNotebook.localLang,
                        $scope.global.model.currentNotebook.foreignLang, selected);

                $state.go(AppStates.TRANSLATE).then(function() {
                    commsPipe.send(Components.READER, Components.TRANSLATE, Subjects.TRANSLATION_REQUEST,
                            translationRequest);
                });

                textSelector.clearSelected();
            }
        };

        var setupClickToTranslate = function(commsPipe, $state, $scope) {

            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });
        };

        var getCsrfAndTriggerLogin = function(jsonWebService) {

            jsonWebService.getCsrfToken();
        };

        var ReaderController = function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            var self = this;

            _.extend(this, abstractMiniAppController);
            self.setupGlobalScope($scope, $state);
            appStates.setMainState(AppStates.READER_MAIN);

            $state.go(AppStates.MAIN).then(function() {
                
                self.setupPageMessages($scope, messageHandler, $timeout);
                self.setupDialogs($scope);
                self.setupSpecialDialogs($scope);
                self.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
                self.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
                
                getCsrfAndTriggerLogin(jsonWebService, $scope);
                setupClickToTranslate(commsPipe, $state, $scope);
            });
        };

        ngRegistrationHelper(linguaApp).registerController(componentName, ngDependencies, ReaderController);
    });
})();
