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
            $scope.model.noteId = null;
        };

        var initDialog = function($scope, languageNamesService) {

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
        
        var subscribeToNotifications = function(commsPipe, $scope, languageNamesService) {
            
            commsPipe.subscribe(Components.ANY, Components.ADD_NOTE, function(note) {

                populateModelFromNote($scope, note);
                initDialog($scope, languageNamesService);
            });
        };
        
        var addSubmitButtonHandler = function($scope, noteService, messageHandler) {
            
            $scope.func.addEditNote = function() {

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.translationSource, $scope.model.noteId);

                noteService.create(note, function(data) {
                    messageHandler.addFreshGlobalMessage($scope, LocalStrings.noteSavedMessage, MessageSeverity.INFO);
                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };
        };
        
        var populateModelFromEnvironment = function($scope, $location) {
            
            $scope.model.foreignLang = $scope.global.model.currentNotebook.sourceLang;
            $scope.model.localLang = $scope.global.model.currentNotebook.targetLang;
            $scope.model.sourceUrl = $location.absUrl();
            $scope.model.translationSource = TranslationSources.MANUAL;
            
            $scope.model.foreignNote = null;
            $scope.model.localNote = null;
            $scope.model.additionalNotes = null;
            $scope.model.noteId = null;
        };

        var AddNoteController = function($scope, $location, noteService, languageNamesService, messageHandler, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            populateModelFromEnvironment($scope, $location);
            initDialog($scope, languageNamesService);
            setDialogTitle($scope);
            addSubmitButtonHandler($scope, noteService, messageHandler);
            subscribeToNotifications(commsPipe, $scope, languageNamesService);
        };

        ngRegistrationHelper(linguaApp).registerController("addNoteController",
                [ "$scope", "$location", "noteService", "languageNamesService", "messageHandler", "commsPipe",
                        AddNoteController ]);
    });
})();
