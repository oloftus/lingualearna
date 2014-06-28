App.Controller.createNew(function() {

    this.isCalled("readerRootController");

    this.imports("controller/abstractRootController");
    this.imports("util/textSelector");
    this.imports("util/appStates");
    this.imports("util/dialogs");
    this.imports("underscore");

    this.loads("controller/readerBarController");
    
    this.injects("$scope");
    this.injects("$state");
    this.injects("$timeout");
    this.injects("service/jsonWebService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractRootController, textSelector, appStates, dialogs, _) {

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

            jsonWebService.acquireCsrfToken();
        };

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
                commsPipe) {

            abstractRootController.setupGlobalScope($scope, $state);
            appStates.setMainState(AppStates.READER_MAIN);
            dialogs.setupDialogs($scope);
            getCsrfAndTriggerLogin(jsonWebService, $scope);
            abstractRootController.setupPageMessages($scope, messageHandler, $timeout);
            abstractRootController.setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            abstractRootController.subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);
            setupClickToTranslate(commsPipe, $state, $scope);
            $state.go(AppStates.MAIN);
        };
    });
});
