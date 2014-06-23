App.Controller.createNew(function() {

    this.isCalled("readerBarController");

    this.injects("$scope");
    this.injects("util/commsPipe");

    this.extends("controller/abstractController");

    this.hasDefinition(function(abstractController) {

        return function($scope, commsPipe) {

            this.setupDefaultScope($scope);
            this.addNotebookChangedHandler(commsPipe, $scope);
        };
    });
});
