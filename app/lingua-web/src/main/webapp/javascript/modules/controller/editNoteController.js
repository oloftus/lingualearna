App.Controller.createNew(function () {

    this.isCalled("editNoteController");

    this.imports("controller/abstractController");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("service/languageService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function (abstractController) {

        var setLanguageTitles = function ($scope, languageService) {

            languageService.lookupLangName($scope.model.foreignLang, function (langName) {
                $scope.model.foreignLangName = langName;
            });

            languageService.lookupLangName($scope.model.localLang, function (langName) {
                $scope.model.localLangName = langName;
            });
        };

        function populateModelFromNote($scope, note) {

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
        };

        var subscribeToEditNoteRequests = function (commsPipe, $scope, languageService) {

            var addNoteHandler = function (note) {

                $scope.model.operationTitle = LocalStrings.editNoteTitle;
                populateModelFromNote($scope, note);
                setLanguageTitles($scope, languageService);
            };

            var subscriberId = commsPipe.subscribe(Components.ANY, Components.EDIT_NOTE, addNoteHandler, Subjects.NOTE);
            abstractController.addCleanupStep($scope, function () {
                commsPipe.unsubscribe(subscriberId);
            });
        };

        var findNoteByNoteId = function($scope, noteId) {

            var searchCriteria = {
                noteId : noteId
            };
            var note = _.findWhere($scope.global.model.currentPage.notes, searchCriteria);

            return note;
        };

        var populateNoteFromResult = function ($scope, updatedNote) {

            var note = findNoteByNoteId($scope, updatedNote.noteId);
            
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
        };

        function setupClickHandlers($scope, noteService, messageHandler) {

            $scope.func.addEditNote = function () {

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                    $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                    $scope.model.translationSource, $scope.model.includedInTest, $scope.model.starred,
                    $scope.model.page.pageId, $scope.model.noteId, $scope.model.position);

                noteService.update($scope.model.noteId, note, function (updatedNote) {
                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.noteSavedMessage, MessageSeverity.INFO);
                    populateNoteFromResult($scope, updatedNote);
                }, function (data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };
        }

        return function ($scope, noteService, languageService, messageHandler, commsPipe) {

            abstractController.setupDefaultScope($scope);
            subscribeToEditNoteRequests(commsPipe, $scope, languageService);
            setupClickHandlers($scope, noteService, messageHandler);
        };
    });
});
