(function() {

    var imports = [ "linguaApp", "controller/abstractController", "util/ngRegistrationHelper", "underscore",
            "util/commsPipe" ];

    define(imports, function(linguaApp, abstractController, ngRegistrationHelper, _) {

        var NotebookHeaderController = function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };

        ngRegistrationHelper(linguaApp).registerController("notebookHeaderController",
                [ "$scope", "commsPipe", NotebookHeaderController ]);
    });
})();
