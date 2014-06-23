App.Controller.createNew(function() {

    this.isCalled("notebookHeaderController");

    this.injects("$scope");
    this.injects("util/commsPipe");

    this.extends("controller/abstractController");

    this.hasDefinition(function() {

        return function($scope, commsPipe) {

            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
