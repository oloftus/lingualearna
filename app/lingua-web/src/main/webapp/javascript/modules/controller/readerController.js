(function() {

    var dependencies = [ "linguaApp", "util/ngRegistrationHelper", "controller/abstractRootController",
            "util/textSelector", "service/jsonWebService", "util/messageHandler", "util/commsPipe",
            "service/notebookService" ];

    define(dependencies, function(linguaApp, ngRegistrationHelper, abstractRootController, textSelector) {

        var setupNotebookEnvironment = function($scope, notebookService, commsPipe, messageHandler) {

            notebookService.getNotebooksAndPages(function(notebooks) {

                $scope.global.model.notebooks = notebooks;

                var done = false;
                _.some(notebooks, function(notebook) {
                    _.some(notebook.pages, function(page) {

                        if (page.lastUsed) {
                            $scope.global.model.currentNotebook = notebook;
                            $scope.global.model.currentPage = page;
                            done = true;
                        }

                        return done;
                    });
                    return done;
                });

                commsPipe.send(Components.READER, Components.ANY, Signals.CurrentNotebookChanged);

            }, function() {
                messageHandler.addFreshPageMessage($scope, LocalStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            });
        };

        var subscribeToNoteSubmissions = function(commsPipe, $scope, notebookService, messageHandler) {

            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, function() {
                setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            }, null, Signals.NoteSubmittedSuccess);
        };

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var translationRequest = new TranslationRequest($scope.global.model.currentNotebook.localLang,
                        $scope.global.model.currentNotebook.foreignLang, selected);

                $state.go(AppStates.TRANSLATE).then(
                        function() {
                            commsPipe.send(Components.READER, Components.TRANSLATE, translationRequest,
                                    Subjects.TranslationRequest);
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
        
        var ReaderController = function($scope, $state, jsonWebService, $timeout, messageHandler, notebookService,
                commsPipe) {

            _.extend(this, abstractRootController);
            this.setMainState(AppStates.READER_MAIN);
            this.setupGlobalScope($scope, $state);
            this.setupPageMessages($scope, messageHandler, $timeout);
            this.setupDialogs($scope);
            this.setupSpecialDialogs($scope);
            
            getCsrfAndTriggerLogin(jsonWebService, $scope);
            setupClickToTranslate(commsPipe, $state, $scope);
            setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            subscribeToNoteSubmissions(commsPipe, $scope, notebookService, messageHandler);

            $state.go(AppStates.READER_MAIN);
        };

        ngRegistrationHelper(linguaApp).registerController(
                "readerController",
                [ "$scope", "$state", "jsonWebService", "$timeout", "messageHandler", "notebookService", "commsPipe",
                        ReaderController ]);
    });
})();
