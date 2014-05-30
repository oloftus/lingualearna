(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/textSelector",
            "util/ngRegistrationHelper", "underscore", "util/commsPipe", "service/notebookService" ];

    define(dependencies, function(linguaApp, abstractController, textSelector, ngRegistrationHelper, _) {

        var mouseupHandler = function(commsPipe, $state, $scope) {

            var selected = textSelector.getSelected().toString();

            if (selected !== "") {
                var translationRequest = new TranslationRequest($scope.global.model.currentNotebook.localLang,
                        $scope.global.model.currentNotebook.foreignLang, selected);

                $state.go(AppStates.TRANSLATE).then(function() {
                    commsPipe.send(Components.READER, Components.TRANSLATE, translationRequest, Subjects.translationRequest);
                });

                textSelector.clearSelected();
            }
        };
        
        var setupClickToTranslate = function(commsPipe, $state, $scope) {
            
            $(document).bind("mouseup", function() {
                mouseupHandler(commsPipe, $state, $scope);
            });
        };
        
        
        var setupNotebookEnvironment = function($scope, notebookService) {
            
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
                        
                        delete page.lastUsed;
                        return done;
                    });
                    return done;
                });
                
            }, function() {
                addFreshGlobalMessage($scope, LocalStrings.genericServerErrorMessage, MessageSeverity.ERROR);
            });
        };
        
        var subscribeToNoteSubmissions = function(commsPipe, $scope, notebookService) {

            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, function(message) {
                if (message === Signals.noteSubmittedSuccessSignal) {
                    setupNotebookEnvironment($scope, notebookService);
                }
            });
        };
        
        var subscribeToCurrentNotebookChangedEvents = function(commsPipe, $scope) {

            $scope.func.currentNotebookChanged = function() {
                commsPipe.send(Components.READER, Components.ANY, Signals.currentNotebookChanged);
            };
        };

        var ReaderController = function($scope, commsPipe, $state, notebookService) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            setupClickToTranslate(commsPipe, $state, $scope);
            subscribeToCurrentNotebookChangedEvents(commsPipe, $scope);
            setupNotebookEnvironment($scope, notebookService);
            subscribeToNoteSubmissions(commsPipe, $scope, notebookService);
        };

        ngRegistrationHelper(linguaApp).registerController("readerController",
                [ "$scope", "commsPipe", "$state", "notebookService", ReaderController ]);
    });
})();
