App.Module.createNew(function() {

    this.isCalled("abstractRootController");
    
    this.imports("underscore");
    this.imports("util/appStates");
    
    this.hasDefinition(function(_, appStates) {

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
        
        var sendCurrentNotebookChangedSignal = function(commsPipe) {
            
            commsPipe.send(Components.READER, Components.ANY, Signals.CURRENT_NOTEBOOK_CHANGED);
        };

        var setupNotebookEnvironment = function($scope, $state, notebookService, commsPipe, messageHandler) {

            var setCurrentNotebookToNewNotebook = function(notebook) {
                
                $scope.global.model.currentNotebook = notebook;
                sendCurrentNotebookChangedSignal(commsPipe);
            };
            
            var successHandler = function(notebooks) {

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

                if (!$scope.global.model.currentNotebook) {
                    if (_.size(notebooks) != 0) {
                        $scope.global.model.currentNotebook = notebooks[0];
                    }
                    else {
                        appStates.goRelative($state, AppStates.ADD_NOTEBOOK, AppStates.NOTEBOOK_MAIN);
                        commsPipe.subscribe(Components.ADD_NOTEBOOK, Components.ANY, setCurrentNotebookToNewNotebook, Signals.NOTEBOOK_SAVED_SUCCESS);
                        return;
                    }
                }
                
                sendCurrentNotebookChangedSignal(commsPipe);
            };
            
            var failureHandler = function() {
                messageHandler.addFreshPageMessage($scope, LocalStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            };
            
            notebookService.getNotebooksAndPages(successHandler, failureHandler);
        };
        
        return {
            setupGlobalScope : setupGlobalScope,
            setupPageMessages : setupPageMessages,
            setupNotebookEnvironment : setupNotebookEnvironment
        };
    });
});
