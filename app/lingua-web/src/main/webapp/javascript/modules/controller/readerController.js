App.Controller.createNew(function() {

    this.isCalled("readerController");

    this.imports("controller/abstractMiniAppController");
    this.imports("util/textSelector");
    this.imports("util/appStates");
    this.imports("underscore");

    this.loads("controller/readerBarController");
    
    this.injects("$scope");
    this.injects("$state");
    this.injects("$timeout");
    this.injects("service/jsonWebService");
    this.injects("service/notebookService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractMiniAppController, textSelector, appStates, _) {

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

        return function($scope, $state, $timeout, jsonWebService, notebookService, messageHandler,
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
    });
});
