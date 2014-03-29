define([ "util/commonTypes", "appRoot", "util/util" ], function() {

    var noteController = function($scope, noteService, languageNamesService, sourceLang, targetLang) {

        $scope.model = {};
        $scope.func = {};

    };

    linguaApp.controller("noteController", [ "$scope", "noteService", "languageNamesService", "sourceLang",
            "targetLang", noteController ]);
});
