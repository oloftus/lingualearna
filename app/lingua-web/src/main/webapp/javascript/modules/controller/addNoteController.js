(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "localization/stringsDefault", "service/languageNamesService", "service/noteService",
            "util/messageHandler", "util/interAppMailbox" ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var populateModel = function($scope, note) {

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

        var AddNoteController = function($scope, noteService, languageNamesService, messageHandler, interAppMailbox) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            setDialogTitle($scope);

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

            interAppMailbox.subscribe(Components.READER, Components.ADD_NOTE, function(messages) {

                var note = _.last(messages);
                populateModel($scope, note);
                initDialog($scope, languageNamesService);
            });

            interAppMailbox.subscribe(Components.TRANSLATE, Components.ADD_NOTE, function(messages) {

                var note = _.last(messages);
                populateModel($scope, note);
                initDialog($scope, languageNamesService);
            });

        };

        ngRegistrationHelper(linguaApp).registerController(
                "addNoteController",
                [ "$scope", "noteService", "languageNamesService", "messageHandler", "interAppMailbox",
                        AddNoteController ]);
    });
})();
