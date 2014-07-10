App.Controller.createNew(function() {

    this.isCalled("noteListController");

    this.imports("jquery");

    this.loads("directive/linguaNote");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("util/messageHandler");

    this.hasDefinition(function($) {

        var NOTEBOOK_CONTAINER = "#notebook-content";
        var NOTES_LIST = "#notes-list";
        var NOTE_HANDLE = ".handle";
        
        var convertZeroBasedIndexToOneBased = function(value) {
            
            return value + 1;
        };
        
        var updateNotePosition = function($scope, noteService, messageHandler, noteId, newPosition) {
            
            var searchCriteria = {
                noteId : noteId
            };
            var note = _.findWhere($scope.global.model.currentPage.notes, searchCriteria);
            
            var oldPosition = note.position;
            newPosition = convertZeroBasedIndexToOneBased(newPosition);
            
            _.each($scope.global.model.currentPage.notes, function(note) {
                if (oldPosition < newPosition) {
                    if (oldPosition <= note.position && note.position <= newPosition) {
                        note.position--;
                    }
                }
                else {
                    if (newPosition <= note.position && note.position < oldPosition) {
                        note.position++;
                    }
                }
            });

            note.position = newPosition;
            
            var failureHandler = function(data, status, headers, config) {
                
                $scope.func.loadNotesIntoPage();
                messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteRearrangeError, MessageSeverity.ERROR);
            };
            
            updateNote(note, $scope, noteService, failureHandler);
        };

        var makeListSortable = function($scope, noteService, messageHandler) {

            $(NOTES_LIST).sortable({
                axis : "y",
                containment : NOTEBOOK_CONTAINER,
                cursor : "move",
                handle : NOTE_HANDLE,
                stop : function(event, ui) {
                    updateNotePosition($scope, noteService, messageHandler, $(ui.item).data().noteid, ui.item.index());
                }
            });
        };

        var updateNote = function(note, $scope, noteService, failureHandler) {

            var noteModel = new Note();
            for (var key in noteModel) {
                noteModel[key] = note[key];
            }
            
            var successHandler = function(updatedModel) {

                for (var key in updatedModel) {
                    note[key] = updatedModel[key];
                }
            };
            
            noteService.update(noteModel.noteId, noteModel, successHandler, failureHandler);
        };

        var setupClickHandlers = function($scope, noteService, messageHandler) {

            $scope.func.doStar = function(note) {

                note.starred = !note.starred;
                
                var failureHandler = function(data, status, headers, config) {
                    
                    note.starred = !note.starred;
                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteDeleteError, MessageSeverity.ERROR);
                };
                
                updateNote(note, $scope, noteService, failureHandler);
            };

            $scope.func.doSetIncludedInTest = function(note) {

                note.includedInTest = !note.includedInTest;
                
                var failureHandler = function(data, status, headers, config) {
                    
                    note.includedInTest = !note.includedInTest;
                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteIncludedInTestError, MessageSeverity.ERROR);
                };
                
                updateNote(note, $scope, noteService, failureHandler);
            };
            
            $scope.func.doDelete = function(note) {
                
                var successHandler = function() {
                    
                    var notes = $scope.global.model.currentPage.notes;
                    var noteIndex = _.indexOf(notes, note);
                    notes.splice(noteIndex, 1);
                };
                
                var failureHandler = function() {
                    
                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteDeleteError, MessageSeverity.ERROR);
                };
                
                noteService.remove(note.noteId, successHandler, failureHandler);
            };
        };

        return function($scope, noteService, messageHandler) {

            makeListSortable($scope, noteService, messageHandler);
            setupClickHandlers($scope, noteService, messageHandler);
        };
    });
});
