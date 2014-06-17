(function() {

    var dependencies = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "util/commsPipe", ];

    define(dependencies, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var ReaderBarController = function($scope, commsPipe, $state) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("readerBarController",
                [ "$scope", "commsPipe", ReaderBarController ]);
    });
})();
