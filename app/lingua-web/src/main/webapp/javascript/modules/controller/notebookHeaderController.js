App.Controller.createNew(function() {

    this.isCalled("notebookHeaderController");

    this.imports("rootApp");
    this.imports("controller/abstractController");
    this.imports("underscore");

    this.importsNg("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(rootApp, abstractController, _) {

        return function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
