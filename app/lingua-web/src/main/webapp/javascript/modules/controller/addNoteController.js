(function() {

    var dependencies = [ "linguaApp", "localization/stringsDefault", "controller/abstractController", "underscore",
            "service/languageNamesService", "service/noteService", "util/messageHandler",
            "underscore" ];

    define(dependencies, function(linguaApp, localStrings, abstractController, _) {

        // TODO: Temporary
        var initParams = {
                foreignLang : "en",
                foreignNote : "",
                localLang : "de",
                localNote : "",
                additionalNotes : "",
                sourceUrl : "",
                noteId : ""
        };

        var formInput = {
            foreignLang : initParams.foreignLang,
            localLang : initParams.localLang,
            foreignNote : initParams.foreignNote,
            localNote : initParams.localNote,
            additionalNotes : initParams.additionalNotes,
            sourceUrl : initParams.sourceUrl,
            noteId : initParams.noteId
        };

        var AddNoteController = function($scope, noteService, languageNamesService, messageHandler, formInput) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $scope.Properties = Properties;
            $scope.model.foreignLang = formInput.foreignLang;
            $scope.model.foreignNote = formInput.foreignNote;
            $scope.model.localLang = formInput.localLang;
            $scope.model.localNote = formInput.localNote;
            $scope.model.additionalNotes = formInput.additionalNotes;
            $scope.model.sourceUrl = formInput.sourceUrl;
            $scope.model.noteId = formInput.noteId;
            $scope.model.operationTitle = localStrings.addNoteTitle;

            $scope.func.addEditNote = function() {

                var note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                        $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl,
                        $scope.model.noteId);

                noteService.create(note, function(data) {
                    messageHandler.addFreshGlobalMessage($scope, localStrings.noteSavedMessage, MessageSeverity.INFO);
                }, function(data, status, headers) {
                    messageHandler.handleErrors($scope, data, status, headers);
                });
            };

            languageNamesService.lookup(new LanguageNameRequest($scope.model.foreignLang), function(data) {
                $scope.model.foreignLangName = data.langName;
            });

            languageNamesService.lookup(new LanguageNameRequest($scope.model.localLang), function(data) {
                $scope.model.localLangName = data.langName;
            });
        };

        linguaApp.provide.constant("formInput", formInput);
        linguaApp.controllerProvider.register("addNoteController", [ "$scope", "noteService", "languageNamesService",
                "messageHandler", "formInput", AddNoteController ]);
    });
})();
