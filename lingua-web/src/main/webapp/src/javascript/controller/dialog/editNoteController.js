define(
    [
        "module/ngModule",
        "types/enums",
        "types/types",
        "config/properties",
        "localization/strings",
        "utility/cleanupManager",
        "utility/objectLocator",
        "service/noteService",
        "service/languageService",
        "ui/notificationManager",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"

    ],
    function (ngModule, enums, types, properties, strings, CleanupManager, ObjectLocator) {

        ngModule.controller("editNoteController",
            [
                "$scope",
                "$timeout",
                "noteService",
                "languageService",
                "notificationManager",
                "messageBus",
                "abstractController",
                "stateNavigator",

                function ($scope, $timeout, noteService, languageService, NotificationHandler, messageBus,
                          AbstractController, stateNavigator) {

                    var notificationManager = new NotificationHandler($scope);
                    var cleanupManager = new CleanupManager($scope);
                    var abstractController = new AbstractController($scope);
                    var objectLocator = new ObjectLocator($scope);

                    function setLanguageTitles() {

                        languageService.lookupLangName($scope.model.foreignLang, function (langName) {
                            $scope.model.foreignLangName = langName;
                        });

                        languageService.lookupLangName($scope.model.localLang, function (langName) {
                            $scope.model.localLangName = langName;
                        });
                    }

                    function populateModelFromNote(note) {

                        $scope.model.noteId = note.noteId;
                        $scope.model.foreignLang = note.foreignLang;
                        $scope.model.foreignNote = note.foreignNote;
                        $scope.model.localLang = note.localLang;
                        $scope.model.localNote = note.localNote;
                        $scope.model.additionalNotes = note.additionalNotes;
                        $scope.model.sourceUrl = note.sourceUrl;
                        $scope.model.translationSource = note.translationSource;
                        $scope.model.includedInTest = note.includedInTest;
                        $scope.model.starred = note.starred;
                        $scope.model.page = $scope.global.model.currentPage;
                        $scope.model.position = note.position;
                    }

                    function subscribeToEditNoteRequests() {

                        function addNoteHandler(note) {

                            $scope.model.operationTitle = strings.editNoteTitle;
                            populateModelFromNote(note);
                            setLanguageTitles();
                        }

                        var subscriberId = messageBus.subscribe(enums.Components.ANY, enums.Components.EDIT_NOTE,
                            addNoteHandler, enums.Subjects.NOTE);
                        cleanupManager.addCleanupStep(function () {
                            messageBus.unsubscribe(subscriberId);
                        });
                    }

                    function populateNoteFromResult(updatedNote) {

                        var note = objectLocator.findNoteByNoteId(updatedNote.noteId);

                        note.foreignLang = updatedNote.foreignLang;
                        note.foreignNote = updatedNote.foreignNote;
                        note.localLang = updatedNote.localLang;
                        note.localNote = updatedNote.localNote;
                        note.additionalNotes = updatedNote.additionalNotes;
                        note.sourceUrl = updatedNote.sourceUrl;
                        note.translationSource = updatedNote.translationSource;
                        note.includedInTest = updatedNote.includedInTest;
                        note.starred = updatedNote.starred;
                        note.position = updatedNote.position;
                    }

                    function editNote() {

                        var pageId = null;

                        if ($scope.model.page) {
                            pageId = $scope.model.page.pageId;
                        }

                        var note = new types.Note($scope.model.foreignLang, $scope.model.foreignNote,
                            $scope.model.localLang, $scope.model.localNote, $scope.model.additionalNotes,
                            $scope.model.sourceUrl, $scope.model.sourceContext, $scope.model.translationSource,
                            $scope.model.includedInTest, $scope.model.starred, pageId,
                            $scope.model.noteId, $scope.model.position);

                        var successHandler = function (updatedNote) {

                            notificationManager.newFormMessage(strings.noteSavedMessage,
                                enums.MessageSeverity.INFO);
                            populateNoteFromResult(updatedNote);
                            messageBus.send(enums.Components.EDIT_NOTE, enums.Components.ANY,
                                enums.Signals.NOTE_SAVED_SUCCESS, updatedNote);

                            $timeout(function () {
                                stateNavigator.goToParentState();
                            }, properties.dialogDisappearTimeout);
                        };

                        var errorHandler = function (data, status, headers) {

                            notificationManager.handleErrors(data, status, headers);
                        };

                        noteService.update($scope.model.noteId, note, successHandler, errorHandler);
                    }

                    function setupClickHandlers() {

                        $scope.func.addEditNote = editNote;
                    }

                    function construct() {

                        abstractController.setup();
                        subscribeToEditNoteRequests();
                        setupClickHandlers();
                    }

                    construct();
                }]);
    });
