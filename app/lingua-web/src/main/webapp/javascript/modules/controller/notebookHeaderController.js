App.Controller.createNew(function() {

    this.isCalled("notebookHeaderController");

    this.imports("controller/abstractController");
    this.imports("underscore");

    this.injects("$scope");
    this.injects("util/commsPipe");

    this.hasDefinition(function(abstractController, _) {

        return function($scope, commsPipe) {

            _.extend(this, abstractController);
            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
