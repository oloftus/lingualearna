App.Controller.createNew(function() {

    this.moduleHasName("editNoteController");

    this.injects("$scope");
    this.injects("service/noteService");
    this.injects("service/languageService");
    this.injects("util/messageHandler");
    
    this.usesConstant("noteId");

    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        /*
         * Prevent users overwriting notes because their original content
         * couldn't be loaded
         */
        var disableSave = function($scope) {

            delete $scope.func.addEditNote;
        };

        var loadResultIntoModel = function(model, result) {

            model.foreignLang = result.foreignLang;
            model.foreignNote = result.foreignNote;
            model.localLang = result.localLang;
            model.localNote = result.localNote;
            model.additionalNotes = result.additionalNotes;
            model.sourceUrl = result.sourceUrl;
        };

        var loadLanguageNames = function(languageService, model) {

            languageService.lookupLangName(new LanguageNameRequest(model.foreignLang), function(data) {
                model.foreignLangName = data.langName;
            });

            languageService.lookupLangName(new LanguageNameRequest(model.localLang), function(data) {
                model.localLangName = data.langName;
            });
        };

        return function($scope, noteService, languageService, messageHandler, noteId) {

            this.setupDefaultScope($scope);

            $scope.model.noteId = noteId;
            $scope.model.operationTitle = localStrings.editNoteTitle;

            $scope.func.addEditNote = function() {

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.translationSource, $scope.model.noteId);

                noteService.update($scope.model.noteId, note, function(data) {
                    messageHandler.addFreshGlobalMessage($scope, localStrings.noteSavedMessage, MessageSeverity.INFO);
                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };

            noteService.retrieve($scope.model.noteId, function(data) {

                loadResultIntoModel($scope.model, data);
                loadLanguageNames(languageService, $scope.model);
            }, function() {

                disableSave($scope);
                messageHandler.addFreshGlobalMessage($scope, LocalStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            });
        };
    });
});
