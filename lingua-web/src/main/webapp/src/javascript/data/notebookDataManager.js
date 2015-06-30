define(
    [
        "module/ngModule",
        "types/enums",
        "localization/strings",
        "underscore",
        "utility/objectLocator",
        "utility/messageBus",
        "service/notebookService",
        "state/stateNavigator",
        "ui/notificationManager"
    ],
    function (ngModule, enums, strings, _, ObjectLocator) {

        ngModule.factory("notebookDataManager",
            [
                "messageBus",
                "notebookService",
                "stateNavigator",
                "notificationManager",

                function (messageBus, notebookService, stateNavigator, NotificationHandler) {

                    return function ($scope) {

                        var notificationManager = new NotificationHandler($scope);
                        var objectLocator = new ObjectLocator($scope);

                        function addNotebookChangedHandler() {

                            $scope.global.func.currentNotebookChanged = function () {
                                messageBus.send(enums.Components.READER, enums.Components.ANY,
                                    enums.Signals.CURRENT_NOTEBOOK_CHANGED);
                            };
                        }

                        function setupNotebookEnvironment() {

                            function setCurrentNotebookToNewNotebook(notebook) {

                                $scope.global.model.currentNotebook = notebook;
                                messageBus.send(enums.Components.BINDER, enums.Components.ANY,
                                    enums.Signals.CURRENT_NOTEBOOK_CHANGED);
                            }

                            function successHandler(notebooks) {

                                $scope.global.model.notebooks = notebooks;

                                var done = false;
                                _.some(notebooks, function (notebook) {
                                    _.some(notebook.pages, function (page) {

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
                                        stateNavigator.goToRelativeState(enums.AppStates.ADD_NOTEBOOK);
                                        messageBus.subscribe(enums.Components.ADD_NOTEBOOK, enums.Components.ANY,
                                            setCurrentNotebookToNewNotebook,
                                            enums.Signals.NOTEBOOK_SAVED_SUCCESS);
                                        return;
                                    }
                                }

                                messageBus.send(enums.Components.BINDER, enums.Components.ANY,
                                    enums.Signals.CURRENT_NOTEBOOK_CHANGED,
                                    true);
                            }

                            function failureHandler() {
                                notificationManager.newPageMessage(strings.genericServerErrorMessage,
                                    enums.MessageSeverity.ERROR);
                            }

                            notebookService.getNotebooksAndPages(successHandler, failureHandler);
                        }

                        function setupCurrentNotebookChangeHandler() {

                            function currentNotebookChangedHandler(wasInitialLoad) {

                                if (!wasInitialLoad) {
                                    var currentNotebookPages = $scope.global.model.currentNotebook.pages;
                                    $scope.global.model.currentPage = _.size(currentNotebookPages) > 0 ? currentNotebookPages[0] : null;
                                }

                                messageBus.send(enums.Components.BINDER, enums.Components.ANY,
                                    enums.Signals.CURRENT_PAGE_CHANGED);
                            }

                            messageBus.subscribe(enums.Components.ANY, enums.Components.ANY,
                                currentNotebookChangedHandler,
                                enums.Signals.CURRENT_NOTEBOOK_CHANGED);
                        }

                        function setupNotebookAddedListener() {

                            function appendNotebook(notebook) {

                                $scope.global.model.notebooks.push(notebook);
                            }

                            messageBus.subscribe(enums.Components.ADD_NOTEBOOK, enums.Components.ANY, appendNotebook,
                                enums.Signals.NOTEBOOK_SAVED_SUCCESS);
                        }

                        function setupNoteAddedListener() {

                            function appendNote(note) {

                                if (note.pageId === $scope.global.model.currentPage.pageId
                                    && $scope.global.model.currentPage.notes) {
                                    $scope.global.model.currentPage.notes.push(note);
                                }
                            }

                            messageBus.subscribe(enums.Components.ADD_NOTE, enums.Components.ANY, appendNote,
                                enums.Signals.NOTE_SAVED_SUCCESS);
                        }

                        function setupNoteUpdatedListener() {

                            function updateNote(newNote) {

                                var notes = $scope.global.model.currentPage.notes;

                                var oldNote = objectLocator.findNoteByNoteId(newNote.noteId);
                                var noteIndex = notes.indexOf(oldNote);

                                if (newNote.pageId === $scope.global.model.currentPage.pageId) {
                                    notes[noteIndex] = newNote;
                                }
                                else {
                                    delete notes[noteIndex];
                                }
                            }

                            messageBus.subscribe(enums.Components.EDIT_NOTE, enums.Components.ANY, updateNote,
                                enums.Signals.NOTE_SAVED_SUCCESS);
                        }

                        function setupPageAddedListener() {

                            function appendPage(page) {

                                $scope.global.model.currentNotebook.pages.push(page);

                                if (!$scope.global.model.currentPage) {
                                    $scope.global.model.currentPage = page;
                                }
                            }

                            messageBus.subscribe(enums.Components.ADD_PAGE, enums.Components.ANY, appendPage,
                                enums.Signals.PAGE_SAVED_SUCCESS);
                        }

                        function setup() {

                            setupCurrentNotebookChangeHandler();
                            setupNotebookEnvironment();
                            setupNotebookAddedListener();
                            setupNoteAddedListener();
                            setupNoteUpdatedListener();
                            setupPageAddedListener();
                            addNotebookChangedHandler();
                        }

                        function doExport() {

                            this.setup = setup;
                        }

                        doExport.call(this);
                    }
                }
            ])
    });