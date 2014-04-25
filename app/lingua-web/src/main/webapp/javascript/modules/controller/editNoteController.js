(function() {

    var dependencies = [ "linguaApp", "localization/stringsDefault", "controller/abstractController", "underscore",
            "util/commonTypes", "util/messageHandler", "service/languageNamesService", "service/noteService",
            "config/properties" ];

    define(dependencies, function(linguaApp, localStrings, abstractController, _) {

        /*
         * Prevent users overwriting notes because their original content
         * couldn't be loaded
         */
        var disableSave = function(scope) {

            delete scope.func.addEditNote;
        };

        var loadResultIntoModel = function(model, result) {

            model.foreignLang = result.foreignLang;
            model.foreignNote = result.foreignNote;
            model.localLang = result.localLang;
            model.localNote = result.localNote;
            model.additionalNotes = result.additionalNotes;
            model.sourceUrl = result.sourceUrl;
        };

        var loadLanguageNames = function(languageNamesService, model) {

            languageNamesService.lookup(new LanguageNameRequest(model.foreignLang), function(data) {
                model.foreignLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest(model.localLang), function(data) {
                model.localLangName = data.langName;
            });
        };

        var EditNoteController = function($scope, noteService, languageNamesService, messageHandler, noteId) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $scope.Properties = Properties;
            $scope.model.noteId = noteId;
            $scope.model.operationTitle = localStrings.editNoteTitle;

            $scope.func.addEditNote = function() {

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.noteId);

                noteService.update($scope.model.noteId, note, function(data) {
                    messageHandler.addFreshGlobalMessage($scope, localStrings.noteSavedMessage, MessageSeverity.INFO);
                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };

            noteService.retrieve($scope.model.noteId, function(data) {

                loadResultIntoModel($scope.model, data);
                loadLanguageNames(languageNamesService, $scope.model);
            }, function() {

                disableSave($scope);
                messageHandler.addFreshGlobalMessage($scope, localStrings.genericServerErrorMessage,
                        MessageSeverity.ERROR);
            });
        };

        linguaApp.controller("editNoteController", [ "$scope", "noteService", "languageNamesService", "messageHandler",
                "noteId", EditNoteController ]);
    });
})();
