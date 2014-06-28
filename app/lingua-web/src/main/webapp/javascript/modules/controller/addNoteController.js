App.Controller.createNew(function() {

    this.isCalled("addNoteController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.injects("$scope");
    this.injects("$rootScope");
    this.injects("$location");
    this.injects("$timeout");
    this.injects("$state");
    this.injects("service/noteService");
    this.injects("service/languageService");
    this.injects("util/messageHandler");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController, _) {

        var populateModelFromNote = function($scope, note) {

            $scope.model.foreignLang = note.foreignLang;
            $scope.model.foreignNote = note.foreignNote;
            $scope.model.localLang = note.localLang;
            $scope.model.localNote = note.localNote;
            $scope.model.additionalNotes = note.additionalNotes;
            $scope.model.sourceUrl = note.sourceUrl;
            $scope.model.translationSource = note.translationSource;
            $scope.model.includedInTest = note.includedInTest;
        };

        var setLanguageTitles = function($scope, languageService) {

            languageService.lookupLangName($scope.model.foreignLang, function(langName) {
                $scope.model.foreignLangName = langName;
            });

            languageService.lookupLangName($scope.model.localLang, function(langName) {
                $scope.model.localLangName = langName;
            });
        };

        var setDialogTitle = function($scope) {

            $scope.model.operationTitle = LocalStrings.addNoteTitle;
        };

        var subscribeToAddNoteRequests = function(commsPipe, $scope, languageService) {

            var addNoteHandler = function(note, subject) {
                populateModelFromNote($scope, note);
                setLanguageTitles($scope, languageService);
            };

            var subscriberId = commsPipe.subscribe(Components.ANY, Components.ADD_NOTE, addNoteHandler, Subjects.NOTE);
            abstractController.addCleanupStep($scope, function() {
                commsPipe.unsubscribe(subscriberId);
            });
        };

        var addSubmitButtonHandler = function(commsPipe, $scope, noteService, messageHandler, $timeout, $state) {

            $scope.func.addEditNote = function() {

                var noteId = null;
                var pageId = null;
                if (!_.isNull($scope.model.page)) {
                    pageId = $scope.model.page.pageId;
                }

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.translationSource, $scope.model.includedInTest, pageId, noteId);

                noteService.create(note, function(data) {

                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.noteSavedMessage, MessageSeverity.INFO);
                    commsPipe.send(Components.ADD_NOTE, Components.ANY, Signals.NoteSubmittedSuccess);

                    $timeout(function() {
                        $state.go(AppStates.MAIN);
                    }, App.Properties.dialogDisappearTimeout);

                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };
        };
        
        var getSourceUrl = function($location) {
            
            // Current URL starts with app root
            var sourceUrl = null;
            if ($location.absUrl().indexOf(App.Properties.applicationRoot) === -1) {
                sourceUrl = $location.absUrl();
            }
        };

        var populateModel = function($scope, $location) {

            $scope.model.page = $scope.global.model.currentPage;
            $scope.model.foreignLang = $scope.global.model.currentNotebook.foreignLang;
            $scope.model.localLang = $scope.global.model.currentNotebook.localLang;
            $scope.model.sourceUrl = getSourceUrl($location);
            $scope.model.translationSource = TranslationSources.MANUAL;
            $scope.model.includedInTest = true;

            $scope.model.foreignNote = null;
            $scope.model.localNote = null;
            $scope.model.additionalNotes = null;
        };

        var subscribeToCurrentNotebookChangedEvents = function($scope, commsPipe) {

            var currentNotebookChangedHandler = function() {

                var newPagesContainsOldCurrent = _.some($scope.global.model.currentNotebook.pages, function(newPage) {
                    var oldCurrentPage = $scope.model.page;
                    return newPage.pageId === oldCurrentPage.pageId && newPage.name === oldCurrentPage.name;
                });

                if (!newPagesContainsOldCurrent) {
                    $scope.model.page = null;
                }
            };

            var subscriberId = commsPipe.subscribe(Components.READER, Components.ANY, currentNotebookChangedHandler,
                    Signals.CURRENT_NOTEBOOK_CHANGED);
            abstractController.addCleanupStep($scope, function() {
                commsPipe.unsubscribe(subscriberId);
            });

        };

        return function($scope, $rootScope, $location, $timeout, $state, noteService, languageService, messageHandler,
                commsPipe) {

            abstractController.setupDefaultScope($scope);
            abstractController.setupCleanup($scope, $rootScope);
            populateModel($scope, $location);
            setLanguageTitles($scope, languageService);
            setDialogTitle($scope);
            addSubmitButtonHandler(commsPipe, $scope, noteService, messageHandler, $timeout, $state);
            subscribeToCurrentNotebookChangedEvents($scope, commsPipe);
            subscribeToAddNoteRequests(commsPipe, $scope, languageService);
        };
    });
});
