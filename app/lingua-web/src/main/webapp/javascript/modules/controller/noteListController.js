App.Controller.createNew(function() {

    this.isCalled("noteListController");

    this.imports("jquery");
    this.imports("util/sortableListHelper");

    this.loads("directive/linguaNote");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("util/messageHandler");

    this.hasDefinition(function($, sortableListHelper) {

        var NOTEBOOK_CONTAINER = "#notebook-content";
        var NOTES_LIST = "#notes-list";
        var NOTE_HANDLE = ".handle";
        var Y_AXIS = "y";
        var MOVE_CURSOR = "move";

        var findNoteByNoteId = function($scope, noteId) {

            var searchCriteria = {
                noteId : noteId
            };
            var note = _.findWhere($scope.global.model.currentPage.notes, searchCriteria);

            return note;
        };

        var getNoteModelFromNote = function(note) {

            var noteModel = new Note();
            _.each(noteModel, function(value, key) {
                noteModel[key] = note[key];
            });

            return noteModel;
        };

        var getUpdatePageFailureHandler = function($scope, messageHandler) {

            return function(data, status, headers, config) {

                $scope.func.loadNotesIntoPage();
                messageHandler
                        .addFreshPageMessage($scope, LocalStrings.inlineNoteRearrangeError, MessageSeverity.ERROR);
            };
        };

        var getUpdatePageSuccessHandler = function(note) {

            return function(updatedModel) {

                _.each(updatedModel, function(key) {
                    note[key] = updatedModel[key];
                });
            };
        };

        var updateNote = function(note, $scope, noteService, messageHandler) {

            var noteModel = getNoteModelFromNote(note);
            var successHandler = getUpdatePageSuccessHandler(note);
            var failureHandler = getUpdatePageFailureHandler($scope, messageHandler);

            noteService.update(note.noteId, noteModel, successHandler, failureHandler);
        };

        var updateNotePosition = function($scope, noteService, messageHandler, noteId, newPosition) {

            var note = findNoteByNoteId($scope, noteId);
            sortableListHelper.updateNotePositionsInInterim(note, $scope.global.model.currentPage.notes, newPosition);
            updateNote(note, $scope, noteService, messageHandler);
        };

        var getDragStopHandler = function($scope, noteService, messageHandler) {

            return function(event, ui) {

                var pageId = $(ui.item).data().noteid;
                var newPosition = ui.item.index();

                updateNotePosition($scope, noteService, messageHandler, pageId, newPosition);
            };
        };

        var makeListSortable = function($scope, noteService, messageHandler) {

            var dragStopHandler = getDragStopHandler($scope, noteService, messageHandler);

            $(NOTES_LIST).sortable({
                axis : Y_AXIS,
                containment : NOTEBOOK_CONTAINER,
                cursor : MOVE_CURSOR,
                handle : NOTE_HANDLE,
                stop : dragStopHandler
            });
        };

        var setupClickHandlers = function($scope, noteService, messageHandler) {

            $scope.func.doStar = function(note) {

                note.starred = !note.starred;

                var failureHandler = function(data, status, headers, config) {

                    note.starred = !note.starred;
                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteDeleteError,
                            MessageSeverity.ERROR);
                };

                updateNote(note, $scope, noteService, failureHandler);
            };

            $scope.func.doSetIncludedInTest = function(note) {

                note.includedInTest = !note.includedInTest;

                var failureHandler = function(data, status, headers, config) {

                    note.includedInTest = !note.includedInTest;
                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteIncludedInTestError,
                            MessageSeverity.ERROR);
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

                    messageHandler.addFreshPageMessage($scope, LocalStrings.inlineNoteDeleteError,
                            MessageSeverity.ERROR);
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
