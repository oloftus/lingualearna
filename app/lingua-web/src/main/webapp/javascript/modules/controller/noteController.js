define([ "util/commonTypes", "appRoot", "util/util", "util/messageHandlers" ], function() {

    var noteController = function($scope, noteService, languageNamesService, messageHandlers, formInput) {

        var Operations = {
            ADD : "ADD",
            EDIT : "EDIT"
        };

        setupDefaultScope($scope);

        $scope.model.foreignLang = formInput.foreignLang;
        $scope.model.foreignNote = formInput.foreignNote;
        $scope.model.localLang = formInput.localLang;
        $scope.model.localNote = formInput.localNote;
        $scope.model.additionalNotes = formInput.additionalNotes;
        $scope.model.sourceUrl = formInput.sourceUrl;
        $scope.model.noteId = formInput.noteId;

        var operation;
        if ($scope.model.noteId == null) {
            operation = Operations.ADD;
            $scope.model.operationTitle = localStrings.addNoteTitle;
        }
        else {
            operation = Operations.EDIT;
            $scope.model.operationTitle = localStrings.editNoteTitle;
        }

        $scope.func.addEditNote = function() {

            note = new Note($scope.model.foreignLang, $scope.model.foreignNote, $scope.model.localLang,
                    $scope.model.localNote, $scope.model.additionalNotes, $scope.model.sourceUrl, $scope.model.noteId);

            if (operation == Operations.ADD) {
                noteService.create(note, function(data) {

                    messageHandlers.clearAllMessages($scope);
                    messageHandlers.addGlobalMessage($scope, localStrings.noteSavedMessage, MessageSeverity.INFO);
                }, function(data, status, headers) {

                    messageHandlers.handleErrors($scope, data, status, headers);
                });
            }
            else {
                noteService.update(note, function(data) {
                    // DO SOMETHING
                }, function(data) {
                    // HANDLE ERROR
                });
            }
        };

        languageNamesService.lookup(new LanguageNameRequest($scope.model.foreignLang), function(data) {
            $scope.model.foreignLangName = data.langName;
        });

        languageNamesService.lookup(new LanguageNameRequest($scope.model.localLang), function(data) {
            $scope.model.localLangName = data.langName;
        });
    };

    linguaApp.controller("noteController", [ "$scope", "noteService", "languageNamesService", "messageHandlers",
            "formInput", noteController ]);
});
