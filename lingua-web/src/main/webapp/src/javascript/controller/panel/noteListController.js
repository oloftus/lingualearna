define(
    [
        "module/ngModule",
        "types/types",
        "types/enums",
        "localization/strings",
        "ui/listManager",
        "utility/objectLocator",
        "jquery",
        "directive/lgNote",
        "service/noteService",
        "ui/notificationManager",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"
    ],
    function (ngModule, types, enums, strings, ListManager, ObjectLocator, $) {

        var NOTEBOOK_CONTAINER = "#lingua-binder-content";
        var NOTES_LIST = "#lingua-notes-list";
        var NOTE_HANDLE = ".lingua-handle";

        ngModule.controller("noteListController",
            [
                "$scope",
                "$state",
                "noteService",
                "notificationManager",
                "messageBus",
                "stateNavigator",
                "abstractController",

                function ($scope, $state, noteService, NotificationManager, messageBus, stateNavigator, AbstractController) {

                    var notificationManager = new NotificationManager($scope);
                    var listManager = new ListManager();
                    var abstractController = new AbstractController($scope);
                    var objectLocator = new ObjectLocator($scope);

                    function getNoteModelFromNote(note) {

                        var noteModel = new types.Note();
                        _.each(noteModel, function (value, key) {
                            noteModel[key] = note[key];
                        });

                        return noteModel;
                    }

                    function getUpdateNoteFailureHandler(note, oldPosition) {

                        return function () {

                            listManager.revertPositions(note, oldPosition);
                            notificationManager
                                .newPageMessage(strings.inlineNoteRearrangeError,
                                enums.MessageSeverity.ERROR);
                        };
                    }

                    function getUpdateNoteSuccessHandler(note) {

                        return function (updatedModel) {

                            _.each(updatedModel, function (key) {
                                note[key] = updatedModel[key];
                            });
                        };
                    }

                    function updateNote(note, failureHandler) {

                        var noteModel = getNoteModelFromNote(note);
                        var successHandler = getUpdateNoteSuccessHandler(note);

                        noteService.update(note.noteId, noteModel, successHandler, failureHandler);
                    }

                    function updateNotePosition(noteId, newPosition) {

                        var note = objectLocator.findNoteByNoteId(noteId);
                        var oldPosition = note.position;
                        var failureHandler = getUpdateNoteFailureHandler(note, oldPosition);

                        listManager.setCollection($scope.global.model.currentPage.notes);
                        listManager.updatePositions(note, newPosition);
                        updateNote(note, failureHandler);
                    }

                    function getDragStopHandler() {

                        return function (event, ui) {

                            var pageId = $(ui.item).data().noteid;
                            var newPosition = ui.item.index();

                            updateNotePosition(pageId, newPosition);
                        };
                    }

                    function setupClickHandlers() {

                        $scope.func.doStar = function (note) {

                            note.starred = !note.starred;

                            function failureHandler() {

                                note.starred = !note.starred;
                                notificationManager.newPageMessage(strings.inlineNoteDeleteError,
                                    enums.MessageSeverity.ERROR);
                            }

                            updateNote(note, failureHandler);
                        };

                        $scope.func.doSetIncludedInTest = function (note) {

                            note.includedInTest = !note.includedInTest;

                            function failureHandler() {

                                note.includedInTest = !note.includedInTest;
                                notificationManager.newPageMessage(strings.inlineNoteIncludedInTestError,
                                    enums.MessageSeverity.ERROR);
                            }

                            updateNote(note, failureHandler);
                        };

                        $scope.func.doDeleteNote = function (note) {

                            function successHandler() {

                                var notes = $scope.global.model.currentPage.notes;
                                var noteIndex = _.indexOf(notes, note);
                                notes.splice(noteIndex, 1);
                            }

                            function failureHandler() {

                                notificationManager.newPageMessage(strings.inlineNoteDeleteError,
                                    enums.MessageSeverity.ERROR);
                            }

                            noteService.remove(note.noteId, successHandler, failureHandler);
                        };

                        $scope.func.doEditNote = function (note) {

                            stateNavigator.goToRelativeState(enums.AppStates.EDIT_NOTE).then(function () {
                                messageBus.send(enums.Components.NOTEBOOK, enums.Components.EDIT_NOTE,
                                    enums.Subjects.NOTE, note);
                            });
                        };
                    }

                    function construct() {

                        abstractController.setup();
                        setupClickHandlers();
                        listManager.setup({
                            list: NOTES_LIST,
                            container: NOTEBOOK_CONTAINER,
                            handle: NOTE_HANDLE,
                            dragHandler: getDragStopHandler()
                        });
                    }

                    construct();
                }
            ])
    });
