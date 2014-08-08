App.Controller.createNew(function() {

    this.isCalled("binderController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.loads("controller/noteListController");
    this.loads("controller/pageListController");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController, _) {

        var loadNotesIntoPage = function($scope, noteService, messageHandler, commsPipe) {

            if ($scope.global.model.currentPage) {
                noteService.getNotesByPage($scope.global.model.currentPage.pageId, function(notes) {
                    $scope.global.model.currentPage.notes = notes;
                }, function(data, status, headers, config) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            }
        };

        var setupPageTabClickHandler = function($scope, commsPipe) {

            $scope.func.changeCurrentPage = function(page) {

                var searchCriteria = {
                    pageId : page.pageId
                };

                var newCurrentPage = _.findWhere($scope.global.model.currentNotebook.pages, searchCriteria);
                $scope.global.model.currentPage = newCurrentPage;

                commsPipe.send(Components.BINDER, Components.ANY, Signals.CURRENT_PAGE_CHANGED);
            };
        };

        var setupNotesReloadConditions = function($scope, noteService, messageHandler, commsPipe) {

            var reloadNotes = function() {
                loadNotesIntoPage($scope, noteService, messageHandler, commsPipe);
            };

            commsPipe.subscribe(Components.NOTEBOOK, Components.ANY, reloadNotes, Signals.APP_LOADED);
            commsPipe.subscribe(Components.BINDER, Components.ANY, reloadNotes, Signals.CURRENT_PAGE_CHANGED);
        };
        
        var setupCurrentNotebookChangeHandler = function($scope, commsPipe) {

            var currentNotebookChangedHandler = function() {

                var currentNotebookPages = $scope.global.model.currentNotebook.pages;
                $scope.global.model.currentPage = _.size(currentNotebookPages) > 0 ? currentNotebookPages[0] : null;

                commsPipe.send(Components.BINDER, Components.ANY, Signals.CURRENT_PAGE_CHANGED);
            };
            commsPipe.subscribe(Components.ANY, Components.ANY, currentNotebookChangedHandler,
                    Signals.CURRENT_NOTEBOOK_CHANGED);
        };
        
        var setupNotebookAddedListener = function($scope, commsPipe) {
            
            var appendNotebook = function(notebook) {
                
                notebook.pages = [];
                $scope.global.model.notebooks.push(notebook);
            };
            
            commsPipe.subscribe(Components.ADD_NOTEBOOK, Components.ANY, appendNotebook, Signals.NOTEBOOK_SAVED_SUCCESS);
        };
        
        var setupNoteAddedListener = function($scope, commsPipe) {
            
            var appendNote = function(note) {
                
                if (note.pageId === $scope.global.model.currentPage.pageId) {
                    $scope.global.model.currentPage.notes.push(note);
                }
            };
            
            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, appendNote, Signals.NOTE_SAVED_SUCCESS);
        };
        
        var setupPageAddedListener = function($scope, commsPipe) {
            
            var appendPage = function(page) {
                
                $scope.global.model.currentNotebook.pages.push(page);
            };
            
            commsPipe.subscribe(Components.ADD_PAGE, Components.ANY, appendPage, Signals.PAGE_SAVED_SUCCESS);
        };

        return function($scope, noteService, messageHandler, commsPipe) {

            abstractController.setupDefaultScope($scope);
            setupPageTabClickHandler($scope, commsPipe);
            setupNotesReloadConditions($scope, noteService, messageHandler, commsPipe);
            setupCurrentNotebookChangeHandler($scope, commsPipe);
            setupNotebookAddedListener($scope, commsPipe);
            setupNoteAddedListener($scope, commsPipe);
            setupPageAddedListener($scope, commsPipe);
        };
    });
});
