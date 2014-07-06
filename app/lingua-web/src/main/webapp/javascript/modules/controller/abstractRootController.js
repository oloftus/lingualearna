App.Module.createNew(function() {

    this.isCalled("abstractRootController");
    
    this.imports("underscore");
    
    this.hasDefinition(function(_) {

        var doCleanup = function($scope) {
            
            _.each($scope.global.cleanupActions, function(action) {
                action();
            });
            $scope.global.cleanupActions = [];
        };
        
        var setupGlobalScope = function($scope, $rootScope) {
            
            $scope.global = {};
            $scope.global.model = {};
            $scope.global.func = {};

            $scope.global.model.pageMessages = [];
            $scope.global.properties = App.Properties;

            $scope.global.cleanupActions = [];
            $scope.global.doCleanup = function() {
                doCleanup($scope);
            };
        };
        
        var setupPageMessages = function($scope, messageHandler, $timeout) {
            
            var pageMessageTimeout = null;
            $scope.$watchCollection("global.model.pageMessages", function(newMessages, oldMessages) {
                
                if (newMessages.length > oldMessages.length) {
                    if (!_.isNull(pageMessageTimeout)) {
                        $timeout.cancel(pageMessageTimeout);
                    }
                    pageMessageTimeout = $timeout(function() {
                        messageHandler.clearPageMessages($scope);
                    }, App.Properties.pageMessagesTimeout);
                }
            });
        };

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

                commsPipe.send(Components.READER, Components.ANY, Signals.CURRENT_NOTEBOOK_CHANGED);

            }, function() {
                messageHandler.addFreshPageMessage($scope, LocalStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            });
        };
        
        var subscribeToNoteSubmissions = function(commsPipe, $scope, notebookService, messageHandler) {

            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, function() {
                setupNotebookEnvironment($scope, notebookService, commsPipe, messageHandler);
            }, Signals.NOTE_SAVED_SUCCESS);
        };
        
        return {
            setupGlobalScope : setupGlobalScope,
            setupPageMessages : setupPageMessages,
            setupNotebookEnvironment : setupNotebookEnvironment, 
            subscribeToNoteSubmissions : subscribeToNoteSubmissions
        };
    });
});
