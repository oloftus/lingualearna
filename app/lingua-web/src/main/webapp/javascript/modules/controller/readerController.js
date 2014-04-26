(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "underscore", "util/commonTypes" ];

    define(dependencies, function(linguaApp, abstractController, _) {

        var ReaderController = function($scope, $location, $route) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);

            $scope.model.currentNotebook = {};
            $scope.model.currentNotebook.url = "url";
            $scope.model.currentNotebook.name = "name";
        };

        linguaApp.controller("readerController", [ "$scope", "$location", "$route", ReaderController ]);
    });
})();
