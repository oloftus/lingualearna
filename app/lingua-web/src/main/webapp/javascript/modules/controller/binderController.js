App.Controller.createNew(function() {

    this.isCalled("binderController");
    
    this.imports("underscore");
    this.imports("controller/abstractController");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(_, abstractController) {

        var loadNotesIntoPage = function($scope, noteService, messageHandler, commsPipe) {
            
            if (!_.isNull($scope.global.model.currentPage)) {
                noteService.getNotesByPage($scope.global.model.currentPage.pageId, function(notes) {
                    $scope.global.model.currentPage.notes = notes;
                }, function(data, status, headers, config) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            }
        };
        
        var setupPageTabClickHandler = function($scope, commsPipe) {
            
            $scope.func.changeCurrentPage = function(pageId) {
                
                var newCurrentPage = _.findWhere($scope.global.model.currentNotebook.pages, { pageId : pageId });
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
            commsPipe.subscribe(Components.ADD_NOTE, Components.ANY, reloadNotes, Signals.NOTE_SUBMITTED_SUCCESS);
        };
        
        var setupCurrentNotebookChangeHandler = function($scope, commsPipe) {
            
            var currentNotebookChangedHandler = function() {
                
                var currentNotebookPages = $scope.global.model.currentNotebook.pages;
                $scope.global.model.currentPage = _.size(currentNotebookPages) > 0 ? currentNotebookPages[0] : null;
                
                commsPipe.send(Components.BINDER, Components.ANY, Signals.CURRENT_PAGE_CHANGED);
            };
            commsPipe.subscribe(Components.ANY, Components.ANY, currentNotebookChangedHandler, Signals.CURRENT_NOTEBOOK_CHANGED);
        };
        
        return function($scope, noteService, messageHandler, commsPipe) {
            
            abstractController.setupDefaultScope($scope);
            setupPageTabClickHandler($scope, commsPipe);
            setupNotesReloadConditions($scope, noteService, messageHandler, commsPipe);
            setupCurrentNotebookChangeHandler($scope, commsPipe);
        };
    });
});
