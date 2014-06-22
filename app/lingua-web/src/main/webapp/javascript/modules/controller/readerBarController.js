App.Controller.createNew(function() {

    this.isCalled("readerBarController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.loads("util/commsPipe");

    this.dependsOnNg("$scope");
    this.dependsOnNg("commsPipe");

    this.hasDefinition(function(abstractController, _) {

        return function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
