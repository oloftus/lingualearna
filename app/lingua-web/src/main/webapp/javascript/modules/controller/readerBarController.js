(function() {

    var imports = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "util/commsPipe", ];

    define(imports, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var ReaderBarController = function($scope, commsPipe, $state) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("readerBarController",
                [ "$scope", "commsPipe", ReaderBarController ]);
    });
})();
