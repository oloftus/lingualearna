define(
    [
        "module/ngModule",
        "types/enums",
        "types/types",
        "config/properties",
        "localization/strings",
        "underscore",
        "utility/cleanupManager",
        "service/noteService",
        "service/languageService",
        "ui/notificationManager",
        "utility/messageBus",
        "state/stateNavigator",
        "controller/abstractController"
    ],
    function (ngModule, enums, types, properties, strings, _, CleanupManager) {

        ngModule.controller("addNoteController",
            [
                "$scope",
                "$rootScope",
                "$location",
                "$timeout",
                "$state",
                "noteService",
                "languageService",
                "notificationManager",
                "messageBus",
                "stateNavigator",
                "abstractController",

                function ($scope, $rootScope, $location, $timeout, $state, noteService, languageService,
                          NotificationHandler, messageBus, stateNavigator, AbstractController) {

                    var notificationManager = new NotificationHandler($scope);
                    var cleanupManager = new CleanupManager($scope);
                    var abstractController = new AbstractController($scope);

                    function populateModelFromNote(note) {

                        $scope.model.foreignLang = note.foreignLang;
                        $scope.model.foreignNote = note.foreignNote;
                        $scope.model.localLang = note.localLang;
                        $scope.model.localNote = note.localNote;
                        $scope.model.additionalNotes = note.additionalNotes;
                        $scope.model.sourceUrl = note.sourceUrl;
                        $scope.model.sourceContext = note.sourceContext;
                        $scope.model.translationSource = note.translationSource;
                        $scope.model.includedInTest = note.includedInTest;
                    }

                    function setLanguageTitles() {

                        languageService.lookupLangName($scope.model.foreignLang, function (langName) {
                            $scope.model.foreignLangName = langName;
                        });

                        languageService.lookupLangName($scope.model.localLang, function (langName) {
                            $scope.model.localLangName = langName;
                        });
                    }

                    function setDialogTitle() {

                        $scope.model.operationTitle = strings.addNoteTitle;
                    }

                    function subscribeToAddNoteRequests() {

                        function addNoteHandler(note) {
                            populateModelFromNote(note);
                            setLanguageTitles();
                        }

                        var subscriberId = messageBus.subscribe(enums.Components.ANY, enums.Components.ADD_NOTE,
                            addNoteHandler, enums.Subjects.NOTE);
                        cleanupManager.addCleanupStep(function () {
                            messageBus.unsubscribe(subscriberId);
                        });
                    }

                    function addNote() {

                        var noteId = null;
                        var pageId = null;
                        if ($scope.model.page) {
                            pageId = $scope.model.page.pageId;
                        }

                        var note = new types.Note($scope.model.foreignLang, $scope.model.foreignNote,
                            $scope.model.localLang, $scope.model.localNote, $scope.model.additionalNotes,
                            $scope.model.sourceUrl, $scope.model.sourceContext, $scope.model.translationSource,
                            $scope.model.includedInTest, $scope.model.starred, pageId, noteId);

                        function successHandler(data) {

                            notificationManager.newFormMessage(strings.noteSavedMessage,
                                enums.MessageSeverity.INFO);
                            messageBus.send(enums.Components.ADD_NOTE, enums.Components.ANY,
                                enums.Signals.NOTE_SAVED_SUCCESS, data);

                            $timeout(function () {
                                stateNavigator.goToParentState();
                            }, properties.dialogDisappearTimeout);
                        }

                        function errorHandler(data, status, headers) {
                            notificationManager.handleErrors(data, status, headers);
                        }

                        noteService.create(note, successHandler, errorHandler);
                    }

                    function setupClickHandlers() {

                        $scope.func.addEditNote = addNote;
                    }

                    function getSourceUrl() {

                        var sourceUrl = null;
                        if ($location.absUrl().indexOf(properties.applicationRoot) === -1) {
                            sourceUrl = $location.absUrl();
                        }

                        return sourceUrl;
                    }

                    function populateModelFromEnvironment() {

                        $scope.model.page = $scope.global.model.currentPage;
                        $scope.model.foreignLang = $scope.global.model.currentNotebook.foreignLang;
                        $scope.model.localLang = $scope.global.model.currentNotebook.localLang;
                        $scope.model.sourceUrl = getSourceUrl($location);
                        $scope.model.translationSource = enums.TranslationSources.MANUAL;
                        $scope.model.includedInTest = true;
                        $scope.model.starred = false;

                        $scope.model.foreignNote = null;
                        $scope.model.localNote = null;
                        $scope.model.additionalNotes = null;
                    }

                    function subscribeToCurrentNotebookChangedEvents() {

                        var subscriberId = messageBus.subscribe(enums.Components.READER, enums.Components.ANY,
                            function () {
                                $scope.model.page = null;
                            }, enums.Signals.CURRENT_NOTEBOOK_CHANGED);

                        cleanupManager.addCleanupStep(function () {
                            messageBus.unsubscribe(subscriberId);
                        });
                    }

                    function construct() {

                        abstractController.setup();
                        populateModelFromEnvironment();
                        setLanguageTitles();
                        setDialogTitle();
                        setupClickHandlers();
                        subscribeToCurrentNotebookChangedEvents();
                        subscribeToAddNoteRequests();
                    }

                    construct();
                }]);
    });
