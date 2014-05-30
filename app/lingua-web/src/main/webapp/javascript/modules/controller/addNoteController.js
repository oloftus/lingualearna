(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "localization/stringsDefault", "service/languageNamesService", "service/noteService",
            "util/messageHandler", "util/commsPipe" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var populateModelFromNote = function($scope, note) {

            $scope.model.foreignLang = note.foreignLang;
            $scope.model.foreignNote = note.foreignNote;
            $scope.model.localLang = note.localLang;
            $scope.model.localNote = note.localNote;
            $scope.model.additionalNotes = note.additionalNotes;
            $scope.model.sourceUrl = note.sourceUrl;
            $scope.model.translationSource = note.translationSource;
        };

        var setLanguageTitles = function($scope, languageNamesService) {

            languageNamesService.lookup(new LanguageNameRequest($scope.model.foreignLang), function(data) {
                $scope.model.foreignLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.localLang), function(data) {
                $scope.model.localLangName = data.langName;
            });
        };

        var setDialogTitle = function($scope) {

            $scope.model.operationTitle = LocalStrings.addNoteTitle;
        };

        var subscribeToAddNoteRequests = function(commsPipe, $scope, languageNamesService) {

            commsPipe.subscribe(Components.ANY, Components.ADD_NOTE, function(note, subject) {

                if (subject === Subjects.Note) {
                    populateModelFromNote($scope, note);
                    setLanguageTitles($scope, languageNamesService);
                }
            });
        };

        var addSubmitButtonHandler = function(commsPipe, $scope, noteService, messageHandler) {

            $scope.func.addEditNote = function() {

                var pageId = null;
                if (!_.isNull($scope.model.page)) {
                    pageId = $scope.model.page.pageId;
                }

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.translationSource, pageId, $scope.model.noteId);

                noteService.create(note, function(data) {

                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.noteSavedMessage, MessageSeverity.INFO);
                    commsPipe.send(Components.ADD_NOTE, Components.ANY, Signals.NoteSubmittedSuccess);

                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };
        };

        var populateModel = function($scope, $location) {

            $scope.model.page = $scope.global.model.currentPage;
            $scope.model.foreignLang = $scope.global.model.currentNotebook.foreignLang;
            $scope.model.localLang = $scope.global.model.currentNotebook.localLang;
            $scope.model.sourceUrl = $location.absUrl();
            $scope.model.translationSource = TranslationSources.MANUAL;

            $scope.model.foreignNote = null;
            $scope.model.localNote = null;
            $scope.model.additionalNotes = null;
            $scope.model.noteId = null;
        };

        var subscribeToCurrentNotebookChangedEvents = function($scope, commsPipe) {

            commsPipe.subscribe(Components.READER, Components.ANY, function(message) {
                if (message === Signals.CurrentNotebookChanged) {

                    var newPagesContainsOldCurrent = _.some($scope.global.model.currentNotebook.pages, function(
                            newPage) {

                        var oldCurrentPage = $scope.model.page;
                        return newPage.pageId === oldCurrentPage.pageId && newPage.name === oldCurrentPage.name;
                    });

                    if (!newPagesContainsOldCurrent) {
                        $scope.model.page = null;
                    }
                }
            });
        };

        var AddNoteController = function($scope, $location, noteService, languageNamesService, messageHandler,
                commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            populateModel($scope, $location);
            setLanguageTitles($scope, languageNamesService);
            setDialogTitle($scope);
            addSubmitButtonHandler(commsPipe, $scope, noteService, messageHandler);
            subscribeToAddNoteRequests(commsPipe, $scope, languageNamesService);
            subscribeToCurrentNotebookChangedEvents($scope, commsPipe);
        };

        ngRegistrationHelper(linguaApp).registerController(
                "addNoteController",
                [ "$scope", "$location", "noteService", "languageNamesService", "messageHandler", "commsPipe",
                        AddNoteController ]);
    });
})();
